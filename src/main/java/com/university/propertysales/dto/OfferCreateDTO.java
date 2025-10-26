package com.university.propertysales.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class OfferCreateDTO {
    @NotNull(message = "Property ID is required")
    private Long propertyId;

    @NotNull(message = "Buyer ID is required")
    private Long buyerId;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private String terms;

    // Constructors
    public OfferCreateDTO() {}

    // Getters and Setters
    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long propertyId) { this.propertyId = propertyId; }

    public Long getBuyerId() { return buyerId; }
    public void setBuyerId(Long buyerId) { this.buyerId = buyerId; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getTerms() { return terms; }
    public void setTerms(String terms) { this.terms = terms; }
}
