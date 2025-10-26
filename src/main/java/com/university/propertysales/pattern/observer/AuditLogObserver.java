package com.university.propertysales.pattern.observer;

import com.university.propertysales.entity.Property;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Concrete Observer - Logs property status changes for audit purposes
 */
@Component
public class AuditLogObserver implements PropertyStatusObserver {
    
    @Override
    public void update(Property property, Property.PropertyStatus oldStatus, Property.PropertyStatus newStatus) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logEntry = String.format(
            "[AUDIT LOG %s] Property ID: %d, Title: '%s', Status Change: %s -> %s, Owner: %s",
            timestamp,
            property.getId(),
            property.getTitle(),
            oldStatus,
            newStatus,
            property.getSeller().getUsername()
        );
        
        System.out.println("AUDIT LOG: " + logEntry);
        
        // In real implementation, this would write to a database or log file
        // auditLogService.logPropertyStatusChange(property.getId(), oldStatus, newStatus, timestamp);
    }
    
    @Override
    public String getObserverName() {
        return "AuditLogObserver";
    }
}
