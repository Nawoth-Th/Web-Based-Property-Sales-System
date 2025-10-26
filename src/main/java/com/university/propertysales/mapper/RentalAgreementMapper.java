package com.university.propertysales.mapper;

import com.university.propertysales.dto.RentalAgreementCreateDTO;
import com.university.propertysales.dto.RentalAgreementDTO;
import com.university.propertysales.entity.Property;
import com.university.propertysales.entity.RentalAgreement;
import com.university.propertysales.entity.User;
import org.springframework.stereotype.Component;

@Component
public class RentalAgreementMapper {

    public RentalAgreementDTO toDTO(RentalAgreement agreement) {
        if (agreement == null) {
            return null;
        }

        RentalAgreementDTO dto = new RentalAgreementDTO();
        dto.setId(agreement.getId());

        if (agreement.getProperty() != null) {
            dto.setPropertyId(agreement.getProperty().getId());
            dto.setPropertyTitle(agreement.getProperty().getTitle());
        }

        if (agreement.getTenant() != null) {
            dto.setTenantId(agreement.getTenant().getId());
            dto.setTenantUsername(agreement.getTenant().getUsername());
        }

        if (agreement.getLandlord() != null) {
            dto.setLandlordId(agreement.getLandlord().getId());
            dto.setLandlordUsername(agreement.getLandlord().getUsername());
        }

        dto.setRent(agreement.getRent());
        dto.setDurationMonths(agreement.getDurationMonths());
        dto.setStartDate(agreement.getStartDate());
        dto.setStatus(agreement.getStatus() != null ? agreement.getStatus().name() : null);
        dto.setCreatedAt(agreement.getCreatedAt());
        dto.setUpdatedAt(agreement.getUpdatedAt());

        return dto;
    }

    public RentalAgreement toEntity(RentalAgreementCreateDTO dto, Property property, User tenant, User landlord) {
        if (dto == null) {
            return null;
        }

        RentalAgreement agreement = new RentalAgreement();
        agreement.setProperty(property);
        agreement.setTenant(tenant);
        agreement.setLandlord(landlord);
        agreement.setRent(dto.getRent());
        agreement.setDurationMonths(dto.getDurationMonths());
        agreement.setStartDate(dto.getStartDate());

        return agreement;
    }
}
