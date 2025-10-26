package com.university.propertysales.pattern.strategy;

import com.university.propertysales.entity.Property;

/**
 * Concrete Strategy - Standard pricing calculation
 */
public class StandardPricingStrategy implements PricingStrategy {
    
    @Override
    public double calculatePrice(Property property) {
        double basePrice = property.getPrice() != null ? property.getPrice().doubleValue() : 0;
        
        System.out.println("Standard Pricing Strategy: Calculating price for property " + property.getId());
        System.out.println("Base price: Rs " + basePrice);
        
        // Standard pricing - no modifications
        return basePrice;
    }
    
    @Override
    public String getStrategyName() {
        return "Standard Pricing";
    }
}
