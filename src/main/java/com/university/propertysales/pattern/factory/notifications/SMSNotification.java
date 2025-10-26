package com.university.propertysales.pattern.factory.notifications;

import com.university.propertysales.pattern.factory.Notification;

/**
 * Concrete implementation of SMS notification
 */
public class SMSNotification extends Notification {
    
    @Override
    public void send() {
        System.out.println("Sending SMS notification:");
        System.out.println("To: " + recipient);
        System.out.println("Message: " + content);
        // In real implementation, this would integrate with SMS service
    }
    
    @Override
    public String getNotificationType() {
        return "SMS";
    }
}
