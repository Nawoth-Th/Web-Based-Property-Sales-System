package com.university.propertysales.pattern.observer;

import com.university.propertysales.entity.Property;

/**
 * Observer interface for property status changes
 */
public interface PropertyStatusObserver {
    void update(Property property, Property.PropertyStatus oldStatus, Property.PropertyStatus newStatus);
    String getObserverName();
}
