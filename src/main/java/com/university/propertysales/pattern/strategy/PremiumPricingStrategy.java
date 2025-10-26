package com.university.propertysales.pattern.strategy;

import com.university.propertysales.entity.Property;

/**
 * Concrete Strategy - Premium pricing calculation with location and size multipliers
 */
public class PremiumPricingStrategy implements PricingStrategy {
    
    private static final double LOCATION_MULTIPLIER = 1.15;
    private static final double SIZE_MULTIPLIER = 1.10;
    private static final double LUXURY_MULTIPLIER = 1.25;
    
    @Override
    public double calculatePrice(Property property) {
        double basePrice = property.getPrice() != null ? property.getPrice().doubleValue() : 0;
        
        System.out.println("Premium Pricing Strategy: Calculating price for property " + property.getId());
        System.out.println("Base price: Rs " + basePrice);
        
        double calculatedPrice = basePrice;
        
        // Apply location premium
        if (isPremiumLocation(property.getLocation())) {
            calculatedPrice *= LOCATION_MULTIPLIER;
            System.out.println("Applied location premium: " + (LOCATION_MULTIPLIER - 1) * 100 + "%");
        }
        
        // Apply size premium
        if (isLargeProperty(property)) {
            calculatedPrice *= SIZE_MULTIPLIER;
            System.out.println("Applied size premium: " + (SIZE_MULTIPLIER - 1) * 100 + "%");
        }
        
        // Apply luxury premium
        if (isLuxuryProperty(property)) {
            calculatedPrice *= LUXURY_MULTIPLIER;
            System.out.println("Applied luxury premium: " + (LUXURY_MULTIPLIER - 1) * 100 + "%");
        }
        
        System.out.println("Final calculated price: Rs " + calculatedPrice);
        return calculatedPrice;
    }
    
    private boolean isPremiumLocation(String location) {
        // In real implementation, this would check against a database of premium locations
        return location != null && (location.toLowerCase().contains("downtown") || 
                                   location.toLowerCase().contains("city center") ||
                                   location.toLowerCase().contains("premium"));
    }
    
    private boolean isLargeProperty(Property property) {
        // In real implementation, this would check property size
        return property.getTitle() != null && property.getTitle().toLowerCase().contains("large");
    }
    
    private boolean isLuxuryProperty(Property property) {
        // In real implementation, this would check property features
        return property.getTitle() != null && (property.getTitle().toLowerCase().contains("luxury") ||
                                             property.getTitle().toLowerCase().contains("penthouse") ||
                                             property.getTitle().toLowerCase().contains("villa"));
    }
    
    @Override
    public String getStrategyName() {
        return "Premium Pricing";
    }
}
