package com.university.propertysales.pattern.singleton;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Singleton Pattern Implementation
 * Ensures single instance of email configuration throughout the application
 */
@Component
public class EmailConfiguration {
    
    private static EmailConfiguration instance;
    
    @Value("${spring.mail.host:smtp.gmail.com}")
    private String mailHost;
    
    @Value("${spring.mail.port:587}")
    private int mailPort;
    
    @Value("${spring.mail.username:}")
    private String mailUsername;
    
    @Value("${spring.mail.password:}")
    private String mailPassword;
    
    private boolean isConfigured = false;
    
    // Private constructor to prevent instantiation
    private EmailConfiguration() {
        // Constructor is private to prevent external instantiation
    }
    
    /**
     * Get the singleton instance
     * @return The single instance of EmailConfiguration
     */
    public static synchronized EmailConfiguration getInstance() {
        if (instance == null) {
            instance = new EmailConfiguration();
        }
        return instance;
    }
    
    /**
     * Initialize configuration with values
     * @param host SMTP host
     * @param port SMTP port
     * @param username Email username
     * @param password Email password
     */
    public synchronized void initialize(String host, int port, String username, String password) {
        this.mailHost = host;
        this.mailPort = port;
        this.mailUsername = username;
        this.mailPassword = password;
        this.isConfigured = true;
        System.out.println("Email configuration initialized for: " + username);
    }
    
    /**
     * Check if email is properly configured
     * @return true if configured, false otherwise
     */
    public boolean isConfigured() {
        return isConfigured && mailUsername != null && !mailUsername.isEmpty() 
               && mailPassword != null && !mailPassword.isEmpty();
    }
    
    /**
     * Get email configuration status
     * @return Status message
     */
    public String getConfigurationStatus() {
        if (isConfigured()) {
            return "Email configuration is ready for: " + mailUsername;
        } else {
            return "Email configuration is not complete";
        }
    }
    
    // Getters
    public String getMailHost() { return mailHost; }
    public int getMailPort() { return mailPort; }
    public String getMailUsername() { return mailUsername; }
    public String getMailPassword() { return mailPassword; }
    
    // Prevent cloning
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Singleton cannot be cloned");
    }
    
    // Prevent deserialization
    protected Object readResolve() {
        return getInstance();
    }
}
