package com.university.propertysales.controller;

import com.university.propertysales.dto.InquiryCreateDTO;
import com.university.propertysales.dto.InquiryDTO;
import com.university.propertysales.entity.Inquiry;
import com.university.propertysales.entity.Property;
import com.university.propertysales.entity.User;
import com.university.propertysales.mapper.InquiryMapper;
import com.university.propertysales.service.EmailService;
import com.university.propertysales.service.InquiryService;
import com.university.propertysales.service.PropertyService;
import com.university.propertysales.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inquiries")
@CrossOrigin(origins = "*")
public class InquiryController {

    @Autowired
    private InquiryService inquiryService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private UserService userService;

    @Autowired
    private InquiryMapper inquiryMapper;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public ResponseEntity<List<InquiryDTO>> getAllInquiries() {
        List<Inquiry> inquiries = inquiryService.getAllInquiries();
        List<InquiryDTO> inquiryDTOs = inquiries.stream()
                .map(inquiryMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(inquiryDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInquiryById(@PathVariable Long id) {
        Optional<Inquiry> inquiry = inquiryService.getInquiryById(id);
        if (inquiry.isPresent()) {
            InquiryDTO inquiryDTO = inquiryMapper.toDTO(inquiry.get());
            return ResponseEntity.ok(inquiryDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createInquiry(@Valid @RequestBody InquiryCreateDTO inquiryCreateDTO) {
        try {
            Optional<Property> property = propertyService.getPropertyById(inquiryCreateDTO.getPropertyId());
            Optional<User> sender = userService.getUserById(inquiryCreateDTO.getSenderId());

            if (!property.isPresent()) {
                return ResponseEntity.badRequest().body("Property not found");
            }
            if (!sender.isPresent()) {
                return ResponseEntity.badRequest().body("Sender not found");
            }

            Inquiry inquiry = inquiryMapper.toEntity(inquiryCreateDTO, property.get(), sender.get());
            Inquiry createdInquiry = inquiryService.createInquiry(inquiry);

            // Send email notifications
            try {
                emailService.sendInquiryCreatedEmail(
                    sender.get().getEmail(),
                    property.get().getSeller().getEmail(),
                    property.get().getTitle(),
                    createdInquiry.getId()
                );
            } catch (Exception e) {
                System.err.println("Failed to send inquiry creation emails: " + e.getMessage());
            }

            InquiryDTO inquiryDTO = inquiryMapper.toDTO(createdInquiry);
            return ResponseEntity.status(HttpStatus.CREATED).body(inquiryDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInquiry(@PathVariable Long id, @Valid @RequestBody InquiryCreateDTO inquiryUpdateDTO) {
        try {
            Inquiry inquiry = inquiryService.getInquiryById(id)
                    .orElseThrow(() -> new RuntimeException("Inquiry not found"));

            inquiry.setMessage(inquiryUpdateDTO.getMessage());

            Inquiry updatedInquiry = inquiryService.updateInquiry(id, inquiry);
            InquiryDTO inquiryDTO = inquiryMapper.toDTO(updatedInquiry);

            return ResponseEntity.ok(inquiryDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateInquiryStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest request) {
        try {
            Inquiry.InquiryStatus status = Inquiry.InquiryStatus.valueOf(request.getStatus().toUpperCase());
            Inquiry updatedInquiry = inquiryService.updateInquiryStatus(id, status);

            // Send email notifications
            try {
                emailService.sendInquiryStatusUpdateEmail(
                    updatedInquiry.getSender().getEmail(),
                    updatedInquiry.getProperty().getSeller().getEmail(),
                    updatedInquiry.getProperty().getTitle(),
                    status.toString(),
                    updatedInquiry.getId()
                );
            } catch (Exception e) {
                System.err.println("Failed to send inquiry status update emails: " + e.getMessage());
            }

            InquiryDTO inquiryDTO = inquiryMapper.toDTO(updatedInquiry);
            return ResponseEntity.ok(inquiryDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<?> getInquiriesByProperty(@PathVariable Long propertyId) {
        Optional<Property> property = propertyService.getPropertyById(propertyId);
        if (!property.isPresent()) {
            return ResponseEntity.badRequest().body("Property not found");
        }

        List<Inquiry> inquiries = inquiryService.getInquiriesByProperty(property.get());
        List<InquiryDTO> inquiryDTOs = inquiries.stream()
                .map(inquiryMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(inquiryDTOs);
    }

    @GetMapping("/sender/{senderId}")
    public ResponseEntity<?> getInquiriesBySender(@PathVariable Long senderId) {
        Optional<User> sender = userService.getUserById(senderId);
        if (!sender.isPresent()) {
            return ResponseEntity.badRequest().body("Sender not found");
        }

        List<Inquiry> inquiries = inquiryService.getInquiriesBySender(sender.get());
        List<InquiryDTO> inquiryDTOs = inquiries.stream()
                .map(inquiryMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(inquiryDTOs);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getInquiriesByStatus(@PathVariable String status) {
        try {
            Inquiry.InquiryStatus inquiryStatus = Inquiry.InquiryStatus.valueOf(status.toUpperCase());
            List<Inquiry> inquiries = inquiryService.getInquiriesByStatus(inquiryStatus);
            List<InquiryDTO> inquiryDTOs = inquiries.stream()
                    .map(inquiryMapper::toDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(inquiryDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status: " + status);
        }
    }

    @GetMapping("/open")
    public ResponseEntity<List<InquiryDTO>> getOpenInquiries() {
        List<Inquiry> openInquiries = inquiryService.getOpenInquiries();
        List<InquiryDTO> inquiryDTOs = openInquiries.stream()
                .map(inquiryMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(inquiryDTOs);
    }

    @PostMapping("/archive-old")
    public ResponseEntity<?> archiveOldInquiries() {
        try {
            inquiryService.archiveOldInquiries();
            return ResponseEntity.ok().body("Old inquiries archived successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInquiry(@PathVariable Long id) {
        try {
            inquiryService.deleteInquiry(id);
            return ResponseEntity.ok().body("Inquiry deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Inner class for status update request
    public static class StatusUpdateRequest {
        private String status;

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
