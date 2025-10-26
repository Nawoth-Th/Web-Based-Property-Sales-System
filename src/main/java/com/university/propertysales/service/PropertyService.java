package com.university.propertysales.service;

import com.university.propertysales.entity.Property;
import com.university.propertysales.entity.User;
import com.university.propertysales.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {
    
    @Autowired
    private PropertyRepository propertyRepository;

    public Property createProperty(Property property) {
        return propertyRepository.save(property);
    }

    public Optional<Property> getPropertyById(Long id) {
        Property property = propertyRepository.findByIdWithRelationships(id);
        return property != null ? Optional.of(property) : Optional.empty();
    }

    public List<Property> getAllProperties() {
        return propertyRepository.findAllWithRelationships();
    }

    public List<Property> getAvailableProperties() {
        return propertyRepository.findByStatusWithRelationships(Property.PropertyStatus.AVAILABLE);
    }

    public List<Property> getPropertiesByType(Property.PropertyType type) {
        return propertyRepository.findByTypeWithRelationships(type);
    }

    public List<Property> getPropertiesBySeller(User seller) {
        return propertyRepository.findBySellerWithRelationships(seller);
    }

    public List<Property> searchPropertiesByLocation(String location) {
        return propertyRepository.findByLocationContainingIgnoreCase(location);
    }

    public List<Property> getPropertiesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return propertyRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<Property> getPropertiesByRentRange(BigDecimal minRent, BigDecimal maxRent) {
        return propertyRepository.findByRentAmountBetween(minRent, maxRent);
    }

    public List<Property> getAvailablePropertiesByType(Property.PropertyType type) {
        return propertyRepository.findByStatusAndTypeWithRelationships(Property.PropertyStatus.AVAILABLE, type);
    }

    public Property updateProperty(Long id, Property propertyDetails) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        
        property.setTitle(propertyDetails.getTitle());
        property.setType(propertyDetails.getType());
        property.setLocation(propertyDetails.getLocation());
        property.setPrice(propertyDetails.getPrice());
        property.setRentAmount(propertyDetails.getRentAmount());
        property.setDescription(propertyDetails.getDescription());
        property.setStatus(propertyDetails.getStatus());
        
        return propertyRepository.save(property);
    }

    public void deleteProperty(Long id) {
        if (!propertyRepository.existsById(id)) {
            throw new RuntimeException("Property not found");
        }
        propertyRepository.deleteById(id);
    }

    public Property updatePropertyStatus(Long id, Property.PropertyStatus status) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        
        property.setStatus(status);
        return propertyRepository.save(property);
    }
}
