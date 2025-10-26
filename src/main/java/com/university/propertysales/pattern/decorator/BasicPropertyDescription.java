package com.university.propertysales.pattern.decorator;

/**
 * Concrete Component - Basic property description
 */
public class BasicPropertyDescription implements PropertyDescription {
    private String description;
    private double price;
    
    public BasicPropertyDescription(String description, double price) {
        this.description = description;
        this.price = price;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public double getPrice() {
        return price;
    }
}
