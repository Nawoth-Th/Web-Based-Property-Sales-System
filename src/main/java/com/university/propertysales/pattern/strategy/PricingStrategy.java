package com.university.propertysales.pattern.strategy;

import com.university.propertysales.entity.Property;

/**
 * Strategy interface for different pricing calculations
 */
public interface PricingStrategy {
    double calculatePrice(Property property);
    String getStrategyName();
}
