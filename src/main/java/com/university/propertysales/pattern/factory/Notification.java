package com.university.propertysales.pattern.factory;

/**
 * Abstract base class for all notifications
 */
public abstract class Notification {
    protected String content;
    protected String recipient;
    protected String subject;
    
    public abstract void send();
    public abstract String getNotificationType();
    
    // Template method for sending notifications
    public final void sendNotification() {
        validateNotification();
        send();
        logNotification();
    }
    
    protected void validateNotification() {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Notification content cannot be empty");
        }
        if (recipient == null || recipient.trim().isEmpty()) {
            throw new IllegalArgumentException("Recipient cannot be empty");
        }
    }
    
    protected void logNotification() {
        System.out.println("Notification sent via " + getNotificationType() + " to " + recipient);
    }
    
    // Getters and Setters
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
}
