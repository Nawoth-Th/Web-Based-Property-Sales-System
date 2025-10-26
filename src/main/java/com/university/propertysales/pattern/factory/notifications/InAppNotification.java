package com.university.propertysales.pattern.factory.notifications;

import com.university.propertysales.pattern.factory.Notification;

/**
 * Concrete implementation of In-App notification
 */
public class InAppNotification extends Notification {
    
    @Override
    public void send() {
        System.out.println("Sending in-app notification:");
        System.out.println("To: " + recipient);
        System.out.println("Content: " + content);
        // In real implementation, this would store notification in database for user to see
    }
    
    @Override
    public String getNotificationType() {
        return "IN_APP";
    }
}
