package com.university.propertysales.mapper;

import com.university.propertysales.dto.PropertyCreateDTO;
import com.university.propertysales.dto.PropertyDTO;
import com.university.propertysales.dto.PropertyUpdateDTO;
import com.university.propertysales.entity.Property;
import com.university.propertysales.entity.User;
import org.springframework.stereotype.Component;

@Component
public class PropertyMapper {

    public PropertyDTO toDTO(Property property) {
        if (property == null) {
            return null;
        }

        PropertyDTO dto = new PropertyDTO();
        dto.setId(property.getId());
        dto.setTitle(property.getTitle());
        dto.setType(property.getType() != null ? property.getType().name() : null);
        dto.setLocation(property.getLocation());
        dto.setPrice(property.getPrice());
        dto.setRentAmount(property.getRentAmount());
        dto.setDescription(property.getDescription());
        dto.setMainImage(property.getMainImage());
        dto.setImages(property.getImages());
        dto.setStatus(property.getStatus() != null ? property.getStatus().name() : null);

        if (property.getSeller() != null) {
            dto.setSellerId(property.getSeller().getId());
            dto.setSellerUsername(property.getSeller().getUsername());
        }

        dto.setCreatedAt(property.getCreatedAt());
        dto.setUpdatedAt(property.getUpdatedAt());

        return dto;
    }

    public Property toEntity(PropertyCreateDTO dto, User seller) {
        if (dto == null) {
            return null;
        }

        Property property = new Property();
        property.setTitle(dto.getTitle());
        property.setType(Property.PropertyType.valueOf(dto.getType().toUpperCase()));
        property.setLocation(dto.getLocation());
        property.setPrice(dto.getPrice());
        property.setRentAmount(dto.getRentAmount());
        property.setDescription(dto.getDescription());
        property.setSeller(seller);

        return property;
    }

    public void updateEntityFromDTO(PropertyUpdateDTO dto, Property property) {
        if (dto == null || property == null) {
            return;
        }

        if (dto.getTitle() != null) {
            property.setTitle(dto.getTitle());
        }
        if (dto.getType() != null) {
            property.setType(Property.PropertyType.valueOf(dto.getType().toUpperCase()));
        }
        if (dto.getLocation() != null) {
            property.setLocation(dto.getLocation());
        }
        if (dto.getPrice() != null) {
            property.setPrice(dto.getPrice());
        }
        if (dto.getRentAmount() != null) {
            property.setRentAmount(dto.getRentAmount());
        }
        if (dto.getDescription() != null) {
            property.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            property.setStatus(Property.PropertyStatus.valueOf(dto.getStatus().toUpperCase()));
        }
    }
}
