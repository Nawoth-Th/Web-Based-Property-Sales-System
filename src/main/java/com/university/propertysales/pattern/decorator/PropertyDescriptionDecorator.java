package com.university.propertysales.pattern.decorator;

/**
 * Abstract Decorator class
 */
public abstract class PropertyDescriptionDecorator implements PropertyDescription {
    protected PropertyDescription propertyDescription;
    
    public PropertyDescriptionDecorator(PropertyDescription propertyDescription) {
        this.propertyDescription = propertyDescription;
    }
    
    @Override
    public String getDescription() {
        return propertyDescription.getDescription();
    }
    
    @Override
    public double getPrice() {
        return propertyDescription.getPrice();
    }
}
