package com.university.propertysales.pattern.strategy;

import com.university.propertysales.entity.Property;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Concrete Strategy - Discount pricing calculation based on various factors
 */
public class DiscountPricingStrategy implements PricingStrategy {
    
    private static final double EARLY_BIRD_DISCOUNT = 0.90; // 10% discount
    private static final double BULK_DISCOUNT = 0.85; // 15% discount
    private static final double SEASONAL_DISCOUNT = 0.88; // 12% discount
    
    @Override
    public double calculatePrice(Property property) {
        double basePrice = property.getPrice() != null ? property.getPrice().doubleValue() : 0;
        
        System.out.println("Discount Pricing Strategy: Calculating price for property " + property.getId());
        System.out.println("Base price: Rs " + basePrice);
        
        double calculatedPrice = basePrice;
        
        // Apply early bird discount (properties listed for more than 6 months)
        if (isEligibleForEarlyBirdDiscount(property)) {
            calculatedPrice *= EARLY_BIRD_DISCOUNT;
            System.out.println("Applied early bird discount: " + (1 - EARLY_BIRD_DISCOUNT) * 100 + "%");
        }
        
        // Apply bulk discount (multiple properties from same owner)
        if (isEligibleForBulkDiscount(property)) {
            calculatedPrice *= BULK_DISCOUNT;
            System.out.println("Applied bulk discount: " + (1 - BULK_DISCOUNT) * 100 + "%");
        }
        
        // Apply seasonal discount (off-season)
        if (isEligibleForSeasonalDiscount()) {
            calculatedPrice *= SEASONAL_DISCOUNT;
            System.out.println("Applied seasonal discount: " + (1 - SEASONAL_DISCOUNT) * 100 + "%");
        }
        
        System.out.println("Final calculated price: Rs " + calculatedPrice);
        return calculatedPrice;
    }
    
    private boolean isEligibleForEarlyBirdDiscount(Property property) {
        // In real implementation, this would check the property's listing date
        // For demo purposes, we'll assume properties with "old" in title are eligible
        return property.getTitle() != null && property.getTitle().toLowerCase().contains("old");
    }
    
    private boolean isEligibleForBulkDiscount(Property property) {
        // In real implementation, this would check if the seller has multiple properties
        // For demo purposes, we'll assume properties with "bulk" in title are eligible
        return property.getTitle() != null && property.getTitle().toLowerCase().contains("bulk");
    }
    
    private boolean isEligibleForSeasonalDiscount() {
        // In real implementation, this would check current season and market conditions
        // For demo purposes, we'll apply seasonal discount
        return true; // Always apply seasonal discount for demo
    }
    
    @Override
    public String getStrategyName() {
        return "Discount Pricing";
    }
}
