package com.university.propertysales.mapper;

import com.university.propertysales.dto.InquiryCreateDTO;
import com.university.propertysales.dto.InquiryDTO;
import com.university.propertysales.entity.Inquiry;
import com.university.propertysales.entity.Property;
import com.university.propertysales.entity.User;
import org.springframework.stereotype.Component;

@Component
public class InquiryMapper {

    public InquiryDTO toDTO(Inquiry inquiry) {
        if (inquiry == null) {
            return null;
        }

        InquiryDTO dto = new InquiryDTO();
        dto.setId(inquiry.getId());

        if (inquiry.getProperty() != null) {
            dto.setPropertyId(inquiry.getProperty().getId());
            dto.setPropertyTitle(inquiry.getProperty().getTitle());
        }

        if (inquiry.getSender() != null) {
            dto.setSenderId(inquiry.getSender().getId());
            dto.setSenderUsername(inquiry.getSender().getUsername());
        }

        dto.setMessage(inquiry.getMessage());
        dto.setStatus(inquiry.getStatus() != null ? inquiry.getStatus().name() : null);
        dto.setCreatedAt(inquiry.getCreatedAt());
        dto.setUpdatedAt(inquiry.getUpdatedAt());

        return dto;
    }

    public Inquiry toEntity(InquiryCreateDTO dto, Property property, User sender) {
        if (dto == null) {
            return null;
        }

        Inquiry inquiry = new Inquiry();
        inquiry.setProperty(property);
        inquiry.setSender(sender);
        inquiry.setMessage(dto.getMessage());

        return inquiry;
    }
}