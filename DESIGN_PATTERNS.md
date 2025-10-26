# Design Patterns Implementation

This document describes the implementation of various design patterns in the Property Management System.

## Implemented Patterns

### 1. Creational Patterns

#### Factory Method Pattern
**Location**: `src/main/java/com/university/propertysales/pattern/factory/`

**Purpose**: Creates different types of notifications without exposing the creation logic.

**Components**:
- `NotificationFactory`: Factory class with static methods to create notifications
- `Notification`: Abstract base class for all notifications
- `NotificationType`: Enum defining notification types
- Concrete implementations: `EmailNotification`, `SMSNotification`, `PushNotification`, `InAppNotification`

**Usage**:
```java
// Create email notification
Notification email = NotificationFactory.createNotification(NotificationType.EMAIL, content, recipient);
email.sendNotification();

// Create SMS notification
Notification sms = NotificationFactory.createNotification(NotificationType.SMS, content, recipient);
sms.sendNotification();
```

#### Singleton Pattern
**Location**: `src/main/java/com/university/propertysales/pattern/singleton/`

**Purpose**: Ensures single instance of email configuration throughout the application.

**Components**:
- `EmailConfiguration`: Singleton class managing email configuration
- Thread-safe implementation with synchronized methods
- Prevents cloning and deserialization

**Usage**:
```java
// Get singleton instance
EmailConfiguration config = EmailConfiguration.getInstance();

// Initialize configuration
config.initialize("smtp.gmail.com", 587, "user@gmail.com", "password");

// Check configuration status
String status = config.getConfigurationStatus();
```

### 2. Structural Patterns

#### Decorator Pattern
**Location**: `src/main/java/com/university/propertysales/pattern/decorator/`

**Purpose**: Dynamically adds features to property descriptions and adjusts pricing accordingly.

**Components**:
- `PropertyDescription`: Interface defining property description contract
- `BasicPropertyDescription`: Concrete component with basic property info
- `PropertyDescriptionDecorator`: Abstract decorator class
- Concrete decorators: `PremiumLocationDecorator`, `LuxuryAmenitiesDecorator`, `FurnishedDecorator`

**Usage**:
```java
// Create basic property
PropertyDescription property = new BasicPropertyDescription("2BR apartment", 500000.0);

// Add premium location
property = new PremiumLocationDecorator(property);

// Add luxury amenities
property = new LuxuryAmenitiesDecorator(property);

// Add furnished features
property = new FurnishedDecorator(property);

// Get final description and price
String description = property.getDescription();
double price = property.getPrice();
```

### 3. Behavioral Patterns

#### Observer Pattern
**Location**: `src/main/java/com/university/propertysales/pattern/observer/`

**Purpose**: Notifies multiple observers when property status changes.

**Components**:
- `PropertyStatusObserver`: Observer interface
- `PropertyStatusSubject`: Subject managing observers
- Concrete observers: `EmailNotificationObserver`, `AuditLogObserver`, `PropertyMarketUpdateObserver`

**Usage**:
```java
// Create subject and add observers
PropertyStatusSubject subject = new PropertyStatusSubject();
subject.setProperty(property);
subject.addObserver(emailObserver);
subject.addObserver(auditObserver);

// Notify observers of status change
subject.notifyObservers(oldStatus, newStatus);
```

#### Strategy Pattern
**Location**: `src/main/java/com/university/propertysales/pattern/strategy/`

**Purpose**: Provides different pricing calculation algorithms that can be selected at runtime.

**Components**:
- `PricingStrategy`: Strategy interface
- `PricingContext`: Context class using strategies
- Concrete strategies: `StandardPricingStrategy`, `PremiumPricingStrategy`, `DiscountPricingStrategy`

**Usage**:
```java
// Create context with default strategy
PricingContext context = new PricingContext();

// Use standard pricing
context.setPricingStrategy(new StandardPricingStrategy());
double standardPrice = context.calculatePrice(property);

// Switch to premium pricing
context.setPricingStrategy(new PremiumPricingStrategy());
double premiumPrice = context.calculatePrice(property);

// Switch to discount pricing
context.setPricingStrategy(new DiscountPricingStrategy());
double discountPrice = context.calculatePrice(property);
```

## Demo Endpoints

The system provides REST endpoints to demonstrate each pattern:

### All Patterns Demo
```
GET /api/design-patterns/demo?propertyId=1
```

### Individual Pattern Demos
```
GET /api/design-patterns/demo/factory
GET /api/design-patterns/demo/singleton
GET /api/design-patterns/demo/decorator
GET /api/design-patterns/demo/observer?propertyId=1
GET /api/design-patterns/demo/strategy?propertyId=1
```

## Benefits of Each Pattern

### Factory Method Pattern
- **Flexibility**: Easy to add new notification types
- **Encapsulation**: Creation logic is hidden from clients
- **Consistency**: All notifications follow the same interface

### Singleton Pattern
- **Resource Management**: Single configuration instance
- **Global Access**: Available throughout the application
- **Thread Safety**: Synchronized access in multi-threaded environment

### Decorator Pattern
- **Dynamic Behavior**: Add features at runtime
- **Composition over Inheritance**: Flexible feature combination
- **Open/Closed Principle**: Open for extension, closed for modification

### Observer Pattern
- **Loose Coupling**: Subject doesn't know about specific observers
- **Dynamic Relationships**: Add/remove observers at runtime
- **Event-Driven**: Automatic notifications on state changes

### Strategy Pattern
- **Algorithm Selection**: Choose algorithm at runtime
- **Extensibility**: Easy to add new pricing strategies
- **Single Responsibility**: Each strategy handles one pricing approach

## Real-World Applications

### Factory Method Pattern
- Creating different types of notifications (email, SMS, push)
- Generating reports in different formats
- Creating different types of users (buyer, seller, admin)

### Singleton Pattern
- Database connection management
- Configuration management
- Logger instances

### Decorator Pattern
- Adding features to property listings
- Enhancing user profiles with additional information
- Building complex reports with multiple sections

### Observer Pattern
- Property status change notifications
- User activity tracking
- System event logging

### Strategy Pattern
- Different pricing calculation methods
- Various payment processing algorithms
- Multiple search strategies

## Testing the Patterns

1. Start the application
2. Use the demo endpoints to see each pattern in action
3. Check console logs for detailed output
4. Modify the patterns to see how they adapt to changes

## Future Enhancements

- Add more concrete implementations for each pattern
- Implement pattern combinations
- Add unit tests for each pattern
- Create configuration-based pattern selection
- Add pattern performance monitoring
