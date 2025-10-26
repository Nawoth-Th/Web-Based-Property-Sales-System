package com.university.propertysales.pattern.factory.notifications;

import com.university.propertysales.pattern.factory.Notification;

/**
 * Concrete implementation of Push notification
 */
public class PushNotification extends Notification {
    
    @Override
    public void send() {
        System.out.println("Sending push notification:");
        System.out.println("To: " + recipient);
        System.out.println("Title: " + (subject != null ? subject : "Property Update"));
        System.out.println("Body: " + content);
        // In real implementation, this would integrate with push notification service
    }
    
    @Override
    public String getNotificationType() {
        return "PUSH";
    }
}
