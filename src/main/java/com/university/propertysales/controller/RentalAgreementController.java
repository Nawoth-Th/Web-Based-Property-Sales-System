package com.university.propertysales.controller;

import com.university.propertysales.dto.RentalAgreementCreateDTO;
import com.university.propertysales.dto.RentalAgreementDTO;
import com.university.propertysales.entity.RentalAgreement;
import com.university.propertysales.entity.Property;
import com.university.propertysales.entity.User;
import com.university.propertysales.mapper.RentalAgreementMapper;
import com.university.propertysales.service.EmailService;
import com.university.propertysales.service.RentalAgreementService;
import com.university.propertysales.service.PropertyService;
import com.university.propertysales.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rental-agreements")
@CrossOrigin(origins = "*")
public class RentalAgreementController {

    @Autowired
    private RentalAgreementService rentalAgreementService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private UserService userService;

    @Autowired
    private RentalAgreementMapper rentalAgreementMapper;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public ResponseEntity<List<RentalAgreementDTO>> getAllRentalAgreements() {
        List<RentalAgreement> agreements = rentalAgreementService.getAllRentalAgreements();
        List<RentalAgreementDTO> agreementDTOs = agreements.stream()
                .map(rentalAgreementMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(agreementDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRentalAgreementById(@PathVariable Long id) {
        Optional<RentalAgreement> agreement = rentalAgreementService.getRentalAgreementById(id);
        if (agreement.isPresent()) {
            RentalAgreementDTO agreementDTO = rentalAgreementMapper.toDTO(agreement.get());
            return ResponseEntity.ok(agreementDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createRentalAgreement(@Valid @RequestBody RentalAgreementCreateDTO createDTO) {
        try {
            Optional<Property> property = propertyService.getPropertyById(createDTO.getPropertyId());
            Optional<User> tenant = userService.getUserById(createDTO.getTenantId());
            Optional<User> landlord = userService.getUserById(createDTO.getLandlordId());

            if (!property.isPresent()) {
                return ResponseEntity.badRequest().body("Property not found");
            }
            if (!tenant.isPresent()) {
                return ResponseEntity.badRequest().body("Tenant not found");
            }
            if (!landlord.isPresent()) {
                return ResponseEntity.badRequest().body("Landlord not found");
            }

            RentalAgreement agreement = rentalAgreementMapper.toEntity(createDTO, property.get(), tenant.get(), landlord.get());
            RentalAgreement createdAgreement = rentalAgreementService.createRentalAgreement(agreement);

            // Update property status to RENTED
            propertyService.updatePropertyStatus(property.get().getId(), Property.PropertyStatus.RENTED);

            // Send email notifications
            try {
                emailService.sendAgreementCreatedEmail(
                    tenant.get().getEmail(),
                    landlord.get().getEmail(),
                    property.get().getTitle(),
                    createdAgreement.getId()
                );
            } catch (Exception e) {
                System.err.println("Failed to send agreement creation emails: " + e.getMessage());
            }

            RentalAgreementDTO agreementDTO = rentalAgreementMapper.toDTO(createdAgreement);
            return ResponseEntity.status(HttpStatus.CREATED).body(agreementDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRentalAgreement(@PathVariable Long id, @Valid @RequestBody RentalAgreementCreateDTO updateDTO) {
        try {
            RentalAgreement agreement = rentalAgreementService.getRentalAgreementById(id)
                    .orElseThrow(() -> new RuntimeException("Rental agreement not found"));

            agreement.setRent(updateDTO.getRent());
            agreement.setDurationMonths(updateDTO.getDurationMonths());
            agreement.setStartDate(updateDTO.getStartDate());

            RentalAgreement updatedAgreement = rentalAgreementService.updateRentalAgreement(id, agreement);
            RentalAgreementDTO agreementDTO = rentalAgreementMapper.toDTO(updatedAgreement);

            return ResponseEntity.ok(agreementDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateAgreementStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest request) {
        try {
            RentalAgreement.AgreementStatus status = RentalAgreement.AgreementStatus.valueOf(request.getStatus().toUpperCase());
            RentalAgreement updatedAgreement = rentalAgreementService.updateAgreementStatus(id, status);

            // If agreement is terminated or expired, update property status back to available
            if (status == RentalAgreement.AgreementStatus.TERMINATED ||
                    status == RentalAgreement.AgreementStatus.EXPIRED) {
                propertyService.updatePropertyStatus(updatedAgreement.getProperty().getId(), Property.PropertyStatus.AVAILABLE);
            }

            // Send email notifications
            try {
                emailService.sendAgreementStatusUpdateEmail(
                    updatedAgreement.getTenant().getEmail(),
                    updatedAgreement.getLandlord().getEmail(),
                    updatedAgreement.getProperty().getTitle(),
                    status.toString(),
                    updatedAgreement.getId()
                );
            } catch (Exception e) {
                System.err.println("Failed to send agreement status update emails: " + e.getMessage());
            }

            RentalAgreementDTO agreementDTO = rentalAgreementMapper.toDTO(updatedAgreement);
            return ResponseEntity.ok(agreementDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}/extend")
    public ResponseEntity<?> extendRentalAgreement(@PathVariable Long id, @RequestBody ExtendRequest request) {
        try {
            RentalAgreement extendedAgreement = rentalAgreementService.extendRentalAgreement(id, request.getAdditionalMonths());
            RentalAgreementDTO agreementDTO = rentalAgreementMapper.toDTO(extendedAgreement);
            return ResponseEntity.ok(agreementDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<?> getRentalAgreementsByProperty(@PathVariable Long propertyId) {
        Optional<Property> property = propertyService.getPropertyById(propertyId);
        if (!property.isPresent()) {
            return ResponseEntity.badRequest().body("Property not found");
        }

        List<RentalAgreement> agreements = rentalAgreementService.getRentalAgreementsByProperty(property.get());
        List<RentalAgreementDTO> agreementDTOs = agreements.stream()
                .map(rentalAgreementMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(agreementDTOs);
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<?> getRentalAgreementsByTenant(@PathVariable Long tenantId) {
        Optional<User> tenant = userService.getUserById(tenantId);
        if (!tenant.isPresent()) {
            return ResponseEntity.badRequest().body("Tenant not found");
        }

        List<RentalAgreement> agreements = rentalAgreementService.getRentalAgreementsByTenant(tenant.get());
        List<RentalAgreementDTO> agreementDTOs = agreements.stream()
                .map(rentalAgreementMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(agreementDTOs);
    }

    @GetMapping("/landlord/{landlordId}")
    public ResponseEntity<?> getRentalAgreementsByLandlord(@PathVariable Long landlordId) {
        Optional<User> landlord = userService.getUserById(landlordId);
        if (!landlord.isPresent()) {
            return ResponseEntity.badRequest().body("Landlord not found");
        }

        List<RentalAgreement> agreements = rentalAgreementService.getRentalAgreementsByLandlord(landlord.get());
        List<RentalAgreementDTO> agreementDTOs = agreements.stream()
                .map(rentalAgreementMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(agreementDTOs);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getRentalAgreementsByStatus(@PathVariable String status) {
        try {
            RentalAgreement.AgreementStatus agreementStatus = RentalAgreement.AgreementStatus.valueOf(status.toUpperCase());
            List<RentalAgreement> agreements = rentalAgreementService.getRentalAgreementsByStatus(agreementStatus);
            List<RentalAgreementDTO> agreementDTOs = agreements.stream()
                    .map(rentalAgreementMapper::toDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(agreementDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status: " + status);
        }
    }

    @GetMapping("/expiring")
    public ResponseEntity<List<RentalAgreementDTO>> getExpiringAgreements(@RequestParam(defaultValue = "30") int days) {
        LocalDate beforeDate = LocalDate.now().plusDays(days);
        List<RentalAgreement> expiringAgreements = rentalAgreementService.getExpiringAgreements(beforeDate);
        List<RentalAgreementDTO> agreementDTOs = expiringAgreements.stream()
                .map(rentalAgreementMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(agreementDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRentalAgreement(@PathVariable Long id) {
        try {
            rentalAgreementService.deleteRentalAgreement(id);
            return ResponseEntity.ok().body("Rental agreement deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Inner classes for request bodies
    public static class StatusUpdateRequest {
        private String status;

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class ExtendRequest {
        private Integer additionalMonths;

        public Integer getAdditionalMonths() { return additionalMonths; }
        public void setAdditionalMonths(Integer additionalMonths) { this.additionalMonths = additionalMonths; }
    }
}
