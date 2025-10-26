package com.university.propertysales.controller;

import com.university.propertysales.dto.OfferCreateDTO;
import com.university.propertysales.dto.OfferDTO;
import com.university.propertysales.entity.Offer;
import com.university.propertysales.entity.Property;
import com.university.propertysales.entity.User;
import com.university.propertysales.mapper.OfferMapper;
import com.university.propertysales.service.EmailService;
import com.university.propertysales.service.OfferService;
import com.university.propertysales.service.PropertyService;
import com.university.propertysales.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/offers")
@CrossOrigin(origins = "*")
public class OfferController {

    @Autowired
    private OfferService offerService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private UserService userService;

    @Autowired
    private OfferMapper offerMapper;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public ResponseEntity<List<OfferDTO>> getAllOffers() {
        List<Offer> offers = offerService.getAllOffers();
        List<OfferDTO> offerDTOs = offers.stream()
                .map(offerMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(offerDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOfferById(@PathVariable Long id) {
        Optional<Offer> offer = offerService.getOfferById(id);
        if (offer.isPresent()) {
            OfferDTO offerDTO = offerMapper.toDTO(offer.get());
            return ResponseEntity.ok(offerDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createOffer(@Valid @RequestBody OfferCreateDTO offerCreateDTO) {
        try {
            Optional<Property> property = propertyService.getPropertyById(offerCreateDTO.getPropertyId());
            Optional<User> buyer = userService.getUserById(offerCreateDTO.getBuyerId());

            if (!property.isPresent()) {
                return ResponseEntity.badRequest().body("Property not found");
            }
            if (!buyer.isPresent()) {
                return ResponseEntity.badRequest().body("Buyer not found");
            }

            // Check if property is available for sale
            if (property.get().getType() != Property.PropertyType.SALE) {
                return ResponseEntity.badRequest().body("Property is not for sale");
            }
            if (property.get().getStatus() != Property.PropertyStatus.AVAILABLE) {
                return ResponseEntity.badRequest().body("Property is not available");
            }

            Offer offer = offerMapper.toEntity(offerCreateDTO, property.get(), buyer.get());
            Offer createdOffer = offerService.createOffer(offer);

            // Send email notifications
            try {
                emailService.sendOfferCreatedEmail(
                    buyer.get().getEmail(),
                    property.get().getSeller().getEmail(),
                    property.get().getTitle(),
                    createdOffer.getId()
                );
            } catch (Exception e) {
                System.err.println("Failed to send offer creation emails: " + e.getMessage());
            }

            OfferDTO offerDTO = offerMapper.toDTO(createdOffer);
            return ResponseEntity.status(HttpStatus.CREATED).body(offerDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOffer(@PathVariable Long id, @Valid @RequestBody OfferCreateDTO offerUpdateDTO) {
        try {
            Offer offer = offerService.getOfferById(id)
                    .orElseThrow(() -> new RuntimeException("Offer not found"));

            offer.setPrice(offerUpdateDTO.getPrice());
            offer.setTerms(offerUpdateDTO.getTerms());

            Offer updatedOffer = offerService.updateOffer(id, offer);
            OfferDTO offerDTO = offerMapper.toDTO(updatedOffer);

            return ResponseEntity.ok(offerDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateOfferStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest request) {
        try {
            Offer.OfferStatus status = Offer.OfferStatus.valueOf(request.getStatus().toUpperCase());
            Offer updatedOffer = offerService.updateOfferStatus(id, status);

            // If offer is accepted, update property status to SOLD
            if (status == Offer.OfferStatus.ACCEPTED) {
                propertyService.updatePropertyStatus(updatedOffer.getProperty().getId(), Property.PropertyStatus.SOLD);
            }

            // Send email notifications
            try {
                emailService.sendOfferStatusUpdateEmail(
                    updatedOffer.getBuyer().getEmail(),
                    updatedOffer.getProperty().getSeller().getEmail(),
                    updatedOffer.getProperty().getTitle(),
                    status.toString(),
                    updatedOffer.getId()
                );
            } catch (Exception e) {
                System.err.println("Failed to send offer status update emails: " + e.getMessage());
            }

            OfferDTO offerDTO = offerMapper.toDTO(updatedOffer);
            return ResponseEntity.ok(offerDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<?> acceptOffer(@PathVariable Long id) {
        try {
            Offer acceptedOffer = offerService.acceptOffer(id);

            // Update property status to SOLD
            propertyService.updatePropertyStatus(acceptedOffer.getProperty().getId(), Property.PropertyStatus.SOLD);

            // Send email notifications
            try {
                emailService.sendOfferStatusUpdateEmail(
                    acceptedOffer.getBuyer().getEmail(),
                    acceptedOffer.getProperty().getSeller().getEmail(),
                    acceptedOffer.getProperty().getTitle(),
                    "ACCEPTED",
                    acceptedOffer.getId()
                );
            } catch (Exception e) {
                System.err.println("Failed to send offer acceptance emails: " + e.getMessage());
            }

            OfferDTO offerDTO = offerMapper.toDTO(acceptedOffer);
            return ResponseEntity.ok(offerDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/counter")
    public ResponseEntity<?> counterOffer(@PathVariable Long id, @RequestBody CounterOfferRequest request) {
        try {
            Offer counterOfferDetails = new Offer();
            counterOfferDetails.setPrice(request.getPrice());
            counterOfferDetails.setTerms(request.getTerms());

            Offer counterOffer = offerService.counterOffer(id, counterOfferDetails);
            OfferDTO offerDTO = offerMapper.toDTO(counterOffer);

            return ResponseEntity.status(HttpStatus.CREATED).body(offerDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<?> getOffersByProperty(@PathVariable Long propertyId) {
        Optional<Property> property = propertyService.getPropertyById(propertyId);
        if (!property.isPresent()) {
            return ResponseEntity.badRequest().body("Property not found");
        }

        List<Offer> offers = offerService.getOffersByProperty(property.get());
        List<OfferDTO> offerDTOs = offers.stream()
                .map(offerMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(offerDTOs);
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<?> getOffersByBuyer(@PathVariable Long buyerId) {
        Optional<User> buyer = userService.getUserById(buyerId);
        if (!buyer.isPresent()) {
            return ResponseEntity.badRequest().body("Buyer not found");
        }

        List<Offer> offers = offerService.getOffersByBuyer(buyer.get());
        List<OfferDTO> offerDTOs = offers.stream()
                .map(offerMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(offerDTOs);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getOffersByStatus(@PathVariable String status) {
        try {
            Offer.OfferStatus offerStatus = Offer.OfferStatus.valueOf(status.toUpperCase());
            List<Offer> offers = offerService.getOffersByStatus(offerStatus);
            List<OfferDTO> offerDTOs = offers.stream()
                    .map(offerMapper::toDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(offerDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status: " + status);
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<List<OfferDTO>> getPendingOffers() {
        List<Offer> pendingOffers = offerService.getPendingOffers();
        List<OfferDTO> offerDTOs = pendingOffers.stream()
                .map(offerMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(offerDTOs);
    }

    @PostMapping("/expire-old")
    public ResponseEntity<?> expireOldOffers() {
        try {
            offerService.expireOldOffers();
            return ResponseEntity.ok().body("Old offers expired successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOffer(@PathVariable Long id) {
        try {
            offerService.deleteOffer(id);
            return ResponseEntity.ok().body("Offer deleted successfully");
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

    public static class CounterOfferRequest {
        private BigDecimal price;
        private String terms;

        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }

        public String getTerms() { return terms; }
        public void setTerms(String terms) { this.terms = terms; }
    }
}
