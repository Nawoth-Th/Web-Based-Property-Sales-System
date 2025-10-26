package com.university.propertysales.pattern.strategy;

import com.university.propertysales.entity.Property;
import org.springframework.stereotype.Component;

/**
 * Context class that uses a pricing strategy
 */
@Component
public class PricingContext {
    private PricingStrategy pricingStrategy;
    
    public PricingContext() {
        // Default strategy
        this.pricingStrategy = new StandardPricingStrategy();
    }
    
    /**
     * Set the pricing strategy
     * @param pricingStrategy The strategy to use
     */
    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
        System.out.println("Pricing strategy changed to: " + pricingStrategy.getStrategyName());
    }
    
    /**
     * Calculate price using the current strategy
     * @param property The property to calculate price for
     * @return The calculated price
     */
    public double calculatePrice(Property property) {
        System.out.println("Using " + pricingStrategy.getStrategyName() + " to calculate price");
        return pricingStrategy.calculatePrice(property);
    }
    
    /**
     * Get current strategy name
     * @return Current strategy name
     */
    public String getCurrentStrategyName() {
        return pricingStrategy.getStrategyName();
    }
}
