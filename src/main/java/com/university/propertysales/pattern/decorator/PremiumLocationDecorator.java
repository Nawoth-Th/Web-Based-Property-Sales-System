package com.university.propertysales.pattern.decorator;

/**
 * Concrete Decorator - Adds premium location features
 */
public class PremiumLocationDecorator extends PropertyDescriptionDecorator {
    private static final double PREMIUM_LOCATION_MULTIPLIER = 1.2;
    private static final String PREMIUM_LOCATION_TEXT = "Located in premium area with excellent connectivity";
    
    public PremiumLocationDecorator(PropertyDescription propertyDescription) {
        super(propertyDescription);
    }
    
    @Override
    public String getDescription() {
        return propertyDescription.getDescription() + "\n• " + PREMIUM_LOCATION_TEXT;
    }
    
    @Override
    public double getPrice() {
        return propertyDescription.getPrice() * PREMIUM_LOCATION_MULTIPLIER;
    }
}
