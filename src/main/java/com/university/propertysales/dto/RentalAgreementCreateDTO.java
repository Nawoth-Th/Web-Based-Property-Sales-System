package com.university.propertysales.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public class RentalAgreementCreateDTO {
    @NotNull(message = "Property ID is required")
    private Long propertyId;

    @NotNull(message = "Tenant ID is required")
    private Long tenantId;

    @NotNull(message = "Landlord ID is required")
    private Long landlordId;

    @NotNull(message = "Rent amount is required")
    @Positive(message = "Rent must be positive")
    private BigDecimal rent;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    private Integer durationMonths;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    // Constructors
    public RentalAgreementCreateDTO() {}

    // Getters and Setters
    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long propertyId) { this.propertyId = propertyId; }

    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }

    public Long getLandlordId() { return landlordId; }
    public void setLandlordId(Long landlordId) { this.landlordId = landlordId; }

    public BigDecimal getRent() { return rent; }
    public void setRent(BigDecimal rent) { this.rent = rent; }

    public Integer getDurationMonths() { return durationMonths; }
    public void setDurationMonths(Integer durationMonths) { this.durationMonths = durationMonths; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
}
