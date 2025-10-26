package com.university.propertysales.mapper;

import com.university.propertysales.dto.OfferCreateDTO;
import com.university.propertysales.dto.OfferDTO;
import com.university.propertysales.entity.Offer;
import com.university.propertysales.entity.Property;
import com.university.propertysales.entity.User;
import org.springframework.stereotype.Component;

@Component
public class OfferMapper {

    public OfferDTO toDTO(Offer offer) {
        if (offer == null) {
            return null;
        }

        OfferDTO dto = new OfferDTO();
        dto.setId(offer.getId());

        if (offer.getProperty() != null) {
            dto.setPropertyId(offer.getProperty().getId());
            dto.setPropertyTitle(offer.getProperty().getTitle());
        }

        if (offer.getBuyer() != null) {
            dto.setBuyerId(offer.getBuyer().getId());
            dto.setBuyerUsername(offer.getBuyer().getUsername());
        }

        dto.setPrice(offer.getPrice());
        dto.setTerms(offer.getTerms());
        dto.setStatus(offer.getStatus() != null ? offer.getStatus().name() : null);
        dto.setCreatedAt(offer.getCreatedAt());
        dto.setUpdatedAt(offer.getUpdatedAt());

        return dto;
    }

    public Offer toEntity(OfferCreateDTO dto, Property property, User buyer) {
        if (dto == null) {
            return null;
        }

        Offer offer = new Offer();
        offer.setProperty(property);
        offer.setBuyer(buyer);
        offer.setPrice(dto.getPrice());
        offer.setTerms(dto.getTerms());

        return offer;
    }
}
