package com.university.propertysales.pattern.factory;

import com.university.propertysales.pattern.factory.notifications.*;

/**
 * Factory Method Pattern Implementation
 * Creates different types of notifications based on the notification type
 */
public class NotificationFactory {
    
    /**
     * Factory method to create appropriate notification based on type
     * @param notificationType The type of notification to create
     * @return The appropriate notification instance
     */
    public static Notification createNotification(NotificationType notificationType) {
        switch (notificationType) {
            case EMAIL:
                return new EmailNotification();
            case SMS:
                return new SMSNotification();
            case PUSH:
                return new PushNotification();
            case IN_APP:
                return new InAppNotification();
            default:
                throw new IllegalArgumentException("Unknown notification type: " + notificationType);
        }
    }
    
    /**
     * Factory method to create notification with specific content
     * @param notificationType The type of notification
     * @param content The notification content
     * @param recipient The recipient information
     * @return Configured notification instance
     */
    public static Notification createNotification(NotificationType notificationType, String content, String recipient) {
        Notification notification = createNotification(notificationType);
        notification.setContent(content);
        notification.setRecipient(recipient);
        return notification;
    }
}
