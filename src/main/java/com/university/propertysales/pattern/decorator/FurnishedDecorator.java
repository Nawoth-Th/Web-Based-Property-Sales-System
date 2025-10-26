package com.university.propertysales.pattern.decorator;

/**
 * Concrete Decorator - Adds furnished property features
 */
public class FurnishedDecorator extends PropertyDescriptionDecorator {
    private static final double FURNISHED_MULTIPLIER = 1.3;
    private static final String FURNISHED_TEXT = "Fully furnished with modern furniture and appliances";
    
    public FurnishedDecorator(PropertyDescription propertyDescription) {
        super(propertyDescription);
    }
    
    @Override
    public String getDescription() {
        return propertyDescription.getDescription() + "\n• " + FURNISHED_TEXT;
    }
    
    @Override
    public double getPrice() {
        return propertyDescription.getPrice() * FURNISHED_MULTIPLIER;
    }
}
