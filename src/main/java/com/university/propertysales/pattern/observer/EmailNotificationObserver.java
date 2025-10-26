package com.university.propertysales.pattern.observer;

import com.university.propertysales.entity.Property;
import com.university.propertysales.pattern.factory.NotificationFactory;
import com.university.propertysales.pattern.factory.NotificationType;
import org.springframework.stereotype.Component;

/**
 * Concrete Observer - Sends email notifications on property status changes
 */
@Component
public class EmailNotificationObserver implements PropertyStatusObserver {
    
    @Override
    public void update(Property property, Property.PropertyStatus oldStatus, Property.PropertyStatus newStatus) {
        String content = generateEmailContent(property, oldStatus, newStatus);
        String recipient = property.getSeller().getEmail();
        
        System.out.println("Email Notification Observer: Sending email to " + recipient);
        System.out.println("Property: " + property.getTitle());
        System.out.println("Status changed from " + oldStatus + " to " + newStatus);
        System.out.println("Content: " + content);
        
        // In real implementation, this would use the actual email service
        var emailNotification = NotificationFactory.createNotification(NotificationType.EMAIL, content, recipient);
        emailNotification.setSubject("Property Status Update - " + property.getTitle());
        emailNotification.sendNotification();
    }
    
    private String generateEmailContent(Property property, Property.PropertyStatus oldStatus, Property.PropertyStatus newStatus) {
        return String.format(
            "Dear Property Owner,\n\n" +
            "Your property '%s' status has been updated:\n" +
            "Previous Status: %s\n" +
            "New Status: %s\n\n" +
            "Please check your Property Management System dashboard for more details.\n\n" +
            "Best regards,\nProperty Management System Team",
            property.getTitle(), oldStatus, newStatus
        );
    }
    
    @Override
    public String getObserverName() {
        return "EmailNotificationObserver";
    }
}
