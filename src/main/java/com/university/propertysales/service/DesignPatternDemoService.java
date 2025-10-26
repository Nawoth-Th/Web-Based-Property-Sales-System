package com.university.propertysales.service;

import com.university.propertysales.entity.Property;
import com.university.propertysales.pattern.decorator.*;
import com.university.propertysales.pattern.factory.NotificationFactory;
import com.university.propertysales.pattern.factory.NotificationType;
import com.university.propertysales.pattern.observer.*;
import com.university.propertysales.pattern.singleton.EmailConfiguration;
import com.university.propertysales.pattern.strategy.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Demo service to showcase all implemented design patterns
 */
@Service
public class DesignPatternDemoService {
    
    @Autowired
    private PropertyStatusSubject propertyStatusSubject;
    
    @Autowired
    private EmailNotificationObserver emailObserver;
    
    @Autowired
    private AuditLogObserver auditObserver;
    
    @Autowired
    private PropertyMarketUpdateObserver marketObserver;
    
    @Autowired
    private PricingContext pricingContext;
    
    /**
     * Demo Factory Method Pattern
     */
    public void demonstrateFactoryPattern() {
        System.out.println("\n=== FACTORY METHOD PATTERN DEMO ===");
        
        // Create different types of notifications using factory
        var emailNotification = NotificationFactory.createNotification(
            NotificationType.EMAIL, 
            "Your property has been updated", 
            "user@example.com"
        );
        emailNotification.sendNotification();
        
        var smsNotification = NotificationFactory.createNotification(
            NotificationType.SMS, 
            "Property status changed", 
            "+1234567890"
        );
        smsNotification.sendNotification();
        
        var pushNotification = NotificationFactory.createNotification(
            NotificationType.PUSH, 
            "New offer received", 
            "user123"
        );
        pushNotification.sendNotification();
    }
    
    /**
     * Demo Singleton Pattern
     */
    public void demonstrateSingletonPattern() {
        System.out.println("\n=== SINGLETON PATTERN DEMO ===");
        
        // Get singleton instance
        EmailConfiguration config1 = EmailConfiguration.getInstance();
        EmailConfiguration config2 = EmailConfiguration.getInstance();
        
        // Verify it's the same instance
        System.out.println("config1 == config2: " + (config1 == config2));
        
        // Initialize configuration
        config1.initialize("smtp.gmail.com", 587, "demo@gmail.com", "password123");
        
        // Both instances should have the same configuration
        System.out.println("Config1 status: " + config1.getConfigurationStatus());
        System.out.println("Config2 status: " + config2.getConfigurationStatus());
    }
    
    /**
     * Demo Decorator Pattern
     */
    public void demonstrateDecoratorPattern() {
        System.out.println("\n=== DECORATOR PATTERN DEMO ===");
        
        // Create basic property description
        PropertyDescription basicProperty = new BasicPropertyDescription(
            "2 Bedroom apartment in downtown area", 
            500000.0
        );
        
        System.out.println("Basic Property:");
        System.out.println("Description: " + basicProperty.getDescription());
        System.out.println("Price: Rs " + basicProperty.getPrice());
        
        // Add premium location decorator
        PropertyDescription premiumLocation = new PremiumLocationDecorator(basicProperty);
        System.out.println("\nWith Premium Location:");
        System.out.println("Description: " + premiumLocation.getDescription());
        System.out.println("Price: Rs " + premiumLocation.getPrice());
        
        // Add luxury amenities decorator
        PropertyDescription luxuryProperty = new LuxuryAmenitiesDecorator(premiumLocation);
        System.out.println("\nWith Luxury Amenities:");
        System.out.println("Description: " + luxuryProperty.getDescription());
        System.out.println("Price: Rs " + luxuryProperty.getPrice());
        
        // Add furnished decorator
        PropertyDescription furnishedProperty = new FurnishedDecorator(luxuryProperty);
        System.out.println("\nFully Decorated Property:");
        System.out.println("Description: " + furnishedProperty.getDescription());
        System.out.println("Price: Rs " + furnishedProperty.getPrice());
    }
    
    /**
     * Demo Observer Pattern
     */
    public void demonstrateObserverPattern(Property property) {
        System.out.println("\n=== OBSERVER PATTERN DEMO ===");
        
        // Set property for the subject
        propertyStatusSubject.setProperty(property);
        
        // Add observers
        propertyStatusSubject.addObserver(emailObserver);
        propertyStatusSubject.addObserver(auditObserver);
        propertyStatusSubject.addObserver(marketObserver);
        
        System.out.println("Number of observers: " + propertyStatusSubject.getObserverCount());
        
        // Simulate status change
        var oldStatus = Property.PropertyStatus.AVAILABLE;
        var newStatus = Property.PropertyStatus.SOLD;
        
        // Notify all observers
        propertyStatusSubject.notifyObservers(oldStatus, newStatus);
    }
    
    /**
     * Demo Strategy Pattern
     */
    public void demonstrateStrategyPattern(Property property) {
        System.out.println("\n=== STRATEGY PATTERN DEMO ===");
        
        // Use standard pricing strategy
        pricingContext.setPricingStrategy(new StandardPricingStrategy());
        double standardPrice = pricingContext.calculatePrice(property);
        
        // Use premium pricing strategy
        pricingContext.setPricingStrategy(new PremiumPricingStrategy());
        double premiumPrice = pricingContext.calculatePrice(property);
        
        // Use discount pricing strategy
        pricingContext.setPricingStrategy(new DiscountPricingStrategy());
        double discountPrice = pricingContext.calculatePrice(property);
        
        System.out.println("\nPrice Comparison:");
        System.out.println("Standard Price: Rs " + standardPrice);
        System.out.println("Premium Price: Rs " + premiumPrice);
        System.out.println("Discount Price: Rs " + discountPrice);
    }
    
    /**
     * Run all pattern demonstrations
     */
    public void runAllPatternDemos(Property property) {
        System.out.println("🎯 DESIGN PATTERNS DEMONSTRATION");
        System.out.println("=====================================");
        
        demonstrateFactoryPattern();
        demonstrateSingletonPattern();
        demonstrateDecoratorPattern();
        demonstrateObserverPattern(property);
        demonstrateStrategyPattern(property);
        
        System.out.println("\n✅ All design patterns demonstrated successfully!");
    }
}
