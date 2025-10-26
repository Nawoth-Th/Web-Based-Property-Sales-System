package com.university.propertysales.pattern.observer;

import com.university.propertysales.entity.Property;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject interface for property status changes
 */
@Component
public class PropertyStatusSubject {
    private List<PropertyStatusObserver> observers = new ArrayList<>();
    private Property property;
    
    public void setProperty(Property property) {
        this.property = property;
    }
    
    /**
     * Add observer to the list
     * @param observer The observer to add
     */
    public void addObserver(PropertyStatusObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("Observer " + observer.getObserverName() + " added to property " + 
                             (property != null ? property.getId() : "system"));
        }
    }
    
    /**
     * Remove observer from the list
     * @param observer The observer to remove
     */
    public void removeObserver(PropertyStatusObserver observer) {
        observers.remove(observer);
        System.out.println("Observer " + observer.getObserverName() + " removed from property " + 
                         (property != null ? property.getId() : "system"));
    }
    
    /**
     * Notify all observers about status change
     * @param oldStatus Previous status
     * @param newStatus New status
     */
    public void notifyObservers(Property.PropertyStatus oldStatus, Property.PropertyStatus newStatus) {
        System.out.println("Notifying " + observers.size() + " observers about status change from " + 
                         oldStatus + " to " + newStatus);
        
        for (PropertyStatusObserver observer : observers) {
            try {
                observer.update(property, oldStatus, newStatus);
            } catch (Exception e) {
                System.err.println("Error notifying observer " + observer.getObserverName() + ": " + e.getMessage());
            }
        }
    }
    
    /**
     * Get number of observers
     * @return Number of observers
     */
    public int getObserverCount() {
        return observers.size();
    }
}
