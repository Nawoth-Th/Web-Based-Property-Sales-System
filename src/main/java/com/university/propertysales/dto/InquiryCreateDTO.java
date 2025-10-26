package com.university.propertysales.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class InquiryCreateDTO {
    @NotNull(message = "Property ID is required")
    private Long propertyId;

    @NotNull(message = "Sender ID is required")
    private Long senderId;

    @NotBlank(message = "Message is required")
    private String message;

    // Constructors
    public InquiryCreateDTO() {}

    // Getters and Setters
    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long propertyId) { this.propertyId = propertyId; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

