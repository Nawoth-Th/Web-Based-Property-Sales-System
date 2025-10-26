package com.university.propertysales.pattern.factory.notifications;

import com.university.propertysales.pattern.factory.Notification;

/**
 * Concrete implementation of Email notification
 */
public class EmailNotification extends Notification {
    
    @Override
    public void send() {
        System.out.println("Sending email notification:");
        System.out.println("To: " + recipient);
        System.out.println("Subject: " + (subject != null ? subject : "Property Management Notification"));
        System.out.println("Content: " + content);
        // In real implementation, this would integrate with EmailService
    }
    
    @Override
    public String getNotificationType() {
        return "EMAIL";
    }
}
