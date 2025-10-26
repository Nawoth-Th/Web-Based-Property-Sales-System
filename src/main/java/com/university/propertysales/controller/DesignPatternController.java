package com.university.propertysales.controller;

import com.university.propertysales.entity.Property;
import com.university.propertysales.service.DesignPatternDemoService;
import com.university.propertysales.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller to demonstrate design patterns
 */
@RestController
@RequestMapping("/api/design-patterns")
@CrossOrigin(origins = "*")
public class DesignPatternController {
    
    @Autowired
    private DesignPatternDemoService demoService;
    
    @Autowired
    private PropertyService propertyService;
    
    /**
     * Demonstrate all design patterns with a sample property
     * @param propertyId Optional property ID to use for demos
     * @return Success message
     */
    @GetMapping("/demo")
    public ResponseEntity<?> demonstratePatterns(@RequestParam(required = false) Long propertyId) {
        try {
            Property property;
            
            if (propertyId != null) {
                Optional<Property> propertyOpt = propertyService.getPropertyById(propertyId);
                if (propertyOpt.isPresent()) {
                    property = propertyOpt.get();
                } else {
                    return ResponseEntity.badRequest().body("Property not found with ID: " + propertyId);
                }
            } else {
                // Create a sample property for demonstration
                property = createSampleProperty();
            }
            
            // Run all pattern demonstrations
            demoService.runAllPatternDemos(property);
            
            return ResponseEntity.ok("Design patterns demonstrated successfully! Check console logs for details.");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error demonstrating patterns: " + e.getMessage());
        }
    }
    
    /**
     * Demonstrate Factory Method Pattern only
     */
    @GetMapping("/demo/factory")
    public ResponseEntity<?> demonstrateFactoryPattern() {
        try {
            demoService.demonstrateFactoryPattern();
            return ResponseEntity.ok("Factory Method Pattern demonstrated successfully! Check console logs.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Demonstrate Singleton Pattern only
     */
    @GetMapping("/demo/singleton")
    public ResponseEntity<?> demonstrateSingletonPattern() {
        try {
            demoService.demonstrateSingletonPattern();
            return ResponseEntity.ok("Singleton Pattern demonstrated successfully! Check console logs.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Demonstrate Decorator Pattern only
     */
    @GetMapping("/demo/decorator")
    public ResponseEntity<?> demonstrateDecoratorPattern() {
        try {
            demoService.demonstrateDecoratorPattern();
            return ResponseEntity.ok("Decorator Pattern demonstrated successfully! Check console logs.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Demonstrate Observer Pattern only
     */
    @GetMapping("/demo/observer")
    public ResponseEntity<?> demonstrateObserverPattern(@RequestParam(required = false) Long propertyId) {
        try {
            Property property;
            
            if (propertyId != null) {
                Optional<Property> propertyOpt = propertyService.getPropertyById(propertyId);
                if (propertyOpt.isPresent()) {
                    property = propertyOpt.get();
                } else {
                    return ResponseEntity.badRequest().body("Property not found with ID: " + propertyId);
                }
            } else {
                property = createSampleProperty();
            }
            
            demoService.demonstrateObserverPattern(property);
            return ResponseEntity.ok("Observer Pattern demonstrated successfully! Check console logs.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Demonstrate Strategy Pattern only
     */
    @GetMapping("/demo/strategy")
    public ResponseEntity<?> demonstrateStrategyPattern(@RequestParam(required = false) Long propertyId) {
        try {
            Property property;
            
            if (propertyId != null) {
                Optional<Property> propertyOpt = propertyService.getPropertyById(propertyId);
                if (propertyOpt.isPresent()) {
                    property = propertyOpt.get();
                } else {
                    return ResponseEntity.badRequest().body("Property not found with ID: " + propertyId);
                }
            } else {
                property = createSampleProperty();
            }
            
            demoService.demonstrateStrategyPattern(property);
            return ResponseEntity.ok("Strategy Pattern demonstrated successfully! Check console logs.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Create a sample property for demonstration purposes
     */
    private Property createSampleProperty() {
        Property property = new Property();
        property.setId(999L);
        property.setTitle("Luxury Downtown Penthouse with Premium Location");
        property.setDescription("A beautiful luxury property in downtown area");
        property.setLocation("Premium Downtown Area");
        property.setPrice(new java.math.BigDecimal("1500000.0"));
        property.setStatus(Property.PropertyStatus.AVAILABLE);
        
        // Create a mock seller (in real implementation, this would be fetched from database)
        com.university.propertysales.entity.User seller = new com.university.propertysales.entity.User();
        seller.setId(1L);
        seller.setUsername("demo_seller");
        seller.setEmail("seller@example.com");
        property.setSeller(seller);
        
        return property;
    }
}
