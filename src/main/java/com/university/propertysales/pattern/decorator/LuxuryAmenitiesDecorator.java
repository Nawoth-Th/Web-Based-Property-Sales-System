package com.university.propertysales.pattern.decorator;

/**
 * Concrete Decorator - Adds luxury amenities features
 */
public class LuxuryAmenitiesDecorator extends PropertyDescriptionDecorator {
    private static final double LUXURY_AMENITIES_MULTIPLIER = 1.5;
    private static final String LUXURY_AMENITIES_TEXT = "Includes luxury amenities: swimming pool, gym, concierge service";
    
    public LuxuryAmenitiesDecorator(PropertyDescription propertyDescription) {
        super(propertyDescription);
    }
    
    @Override
    public String getDescription() {
        return propertyDescription.getDescription() + "\n• " + LUXURY_AMENITIES_TEXT;
    }
    
    @Override
    public double getPrice() {
        return propertyDescription.getPrice() * LUXURY_AMENITIES_MULTIPLIER;
    }
}
