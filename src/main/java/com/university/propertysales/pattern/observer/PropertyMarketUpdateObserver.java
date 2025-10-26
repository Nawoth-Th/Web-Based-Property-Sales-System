package com.university.propertysales.pattern.observer;

import com.university.propertysales.entity.Property;
import org.springframework.stereotype.Component;

/**
 * Concrete Observer - Updates market statistics when property status changes
 */
@Component
public class PropertyMarketUpdateObserver implements PropertyStatusObserver {
    
    @Override
    public void update(Property property, Property.PropertyStatus oldStatus, Property.PropertyStatus newStatus) {
        System.out.println("Market Update Observer: Updating market statistics");
        System.out.println("Property: " + property.getTitle() + " in " + property.getLocation());
        System.out.println("Status changed from " + oldStatus + " to " + newStatus);
        
        // Update market statistics based on status change
        switch (newStatus) {
            case AVAILABLE:
                updateAvailablePropertiesCount(property);
                break;
            case SOLD:
                updateSoldPropertiesCount(property);
                break;
            case RENTED:
                updateRentedPropertiesCount(property);
                break;
            case MAINTENANCE:
                updateMaintenancePropertiesCount(property);
                break;
        }
        
        System.out.println("Market statistics updated successfully");
    }
    
    private void updateAvailablePropertiesCount(Property property) {
        System.out.println("Market Update: Property " + property.getId() + " is now available in market");
        // In real implementation, this would update market statistics database
    }
    
    private void updateSoldPropertiesCount(Property property) {
        System.out.println("Market Update: Property " + property.getId() + " has been sold");
        // In real implementation, this would update sold properties count
    }
    
    private void updateRentedPropertiesCount(Property property) {
        System.out.println("Market Update: Property " + property.getId() + " has been rented");
        // In real implementation, this would update rented properties count
    }
    
    private void updateMaintenancePropertiesCount(Property property) {
        System.out.println("Market Update: Property " + property.getId() + " is under maintenance");
        // In real implementation, this would update maintenance properties count
    }
    
    @Override
    public String getObserverName() {
        return "PropertyMarketUpdateObserver";
    }
}
