# 🏠 Property Management System

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green?style=for-the-badge&logo=spring)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue?style=for-the-badge&logo=mysql)
![JavaScript](https://img.shields.io/badge/JavaScript-ES6+-yellow?style=for-the-badge&logo=javascript)

**A comprehensive full-stack property management system built with Java Spring Boot and modern web technologies**

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![University Project](https://img.shields.io/badge/University-Project-blue?style=for-the-badge)](https://github.com)

</div>

## 📋 Table of Contents

- [🎯 Overview](#-overview)
- [✨ Features](#-features)
- [🛠️ Technology Stack](#️-technology-stack)
- [🚀 Quick Start](#-quick-start)
- [📁 Project Structure](#-project-structure)
- [🎨 Design Patterns](#-design-patterns)
- [🔐 Demo Accounts](#-demo-accounts)
- [📚 API Documentation](#-api-documentation)
- [🖼️ Screenshots](#️-screenshots)
- [👥 Team](#-team)
- [📄 License](#-license)

## 🎯 Overview

This Property Management System is a comprehensive full-stack web application designed to streamline property sales, rentals, and management processes. Built as a university project, it demonstrates enterprise-level Java development practices, design patterns, and modern web technologies.

### Key Highlights

- 🏗️ **Full-Stack Architecture**: Java Spring Boot backend with vanilla JavaScript frontend
- 🔐 **Role-Based Security**: Multi-role user system with Spring Security
- 📱 **Responsive Design**: Modern UI with CSS Grid and Flexbox
- 🎨 **Design Patterns**: Implementation of 5 key design patterns
- 🗄️ **Robust Database**: MySQL with proper relationships and constraints
- 📧 **Email Notifications**: Automated email system for important events

## ✨ Features

### 👥 User Management
- **Multi-Role System**: BUYER, SELLER, RENTER, AGENT, ADMIN
- **Secure Authentication**: BCrypt password encryption
- **Profile Management**: User registration, login, and profile updates
- **Demo Accounts**: Pre-configured accounts for testing

### 🏘️ Property Management
- **Dual Property Types**: Support for both sales and rentals
- **Advanced Search**: Filter by location, price, type, and status
- **Image Management**: Upload and manage property images
- **Status Tracking**: AVAILABLE, SOLD, RENTED, MAINTENANCE
- **Seller Dashboard**: Manage your property listings

### 📅 Booking System
- **Appointment Scheduling**: Book property visits with date/time selection
- **Conflict Detection**: Automatic detection of booking conflicts
- **Status Management**: PENDING, CONFIRMED, CANCELLED
- **Multi-User Views**: Different views for buyers and sellers

### 🏠 Rental Management
- **Agreement Creation**: Create and manage rental agreements
- **Status Tracking**: ACTIVE, EXPIRED, TERMINATED
- **Extension System**: Extend existing rental agreements
- **Expiration Monitoring**: Track expiring agreements

### 💬 Communication System
- **Property Inquiries**: Send and manage property inquiries
- **Offer System**: Submit, accept, reject, or counter offers
- **Status Tracking**: OPEN, RESOLVED, ARCHIVED for inquiries
- **Email Notifications**: Automated notifications for important events

## 🛠️ Technology Stack

### Backend
- **Java 21** - Latest LTS version with modern features
- **Spring Boot 3.2.0** - Rapid application development framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations and ORM
- **MySQL 8.0+** - Relational database management
- **Maven** - Dependency management and build tool

### Frontend
- **HTML5** - Semantic markup and modern structure
- **CSS3** - Advanced styling with Grid and Flexbox
- **Vanilla JavaScript** - ES6+ features and modern syntax
- **Fetch API** - RESTful API communication

### Database
- **MySQL** - Primary database
- **JPA/Hibernate** - Object-relational mapping
- **Foreign Key Constraints** - Data integrity
- **Enum Types** - Status management

## 🚀 Quick Start

### Prerequisites

- **JDK 21** or higher
- **MySQL 8.0** or higher
- **Maven 3.6** or higher
- **Git** for version control

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/property-management-system.git
   cd property-management-system
   ```

2. **Setup MySQL Database**
   ```sql
   CREATE DATABASE property_sales_db;
   ```

3. **Configure Database Connection**
   
   Update `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/property_sales_db?createDatabaseIfNotExist=true
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

4. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the Application**
   - Open your browser to `http://localhost:8080`
   - Use demo accounts or register new users

## 📁 Project Structure

```
property-management-system/
├── 📁 src/main/java/com/university/propertysales/
│   ├── 📁 entity/              # JPA entities
│   │   ├── User.java
│   │   ├── Property.java
│   │   ├── Booking.java
│   │   ├── RentalAgreement.java
│   │   ├── Inquiry.java
│   │   └── Offer.java
│   ├── 📁 repository/          # Data repositories
│   ├── 📁 service/             # Business logic
│   ├── 📁 controller/           # REST controllers
│   ├── 📁 dto/                  # Data Transfer Objects
│   ├── 📁 mapper/               # Entity-DTO mappers
│   ├── 📁 config/               # Configuration classes
│   └── 📁 pattern/              # Design patterns
│       ├── 📁 factory/          # Factory Method Pattern
│       ├── 📁 singleton/        # Singleton Pattern
│       ├── 📁 decorator/        # Decorator Pattern
│       ├── 📁 observer/         # Observer Pattern
│       └── 📁 strategy/           # Strategy Pattern
├── 📁 src/main/resources/
│   ├── 📁 static/               # Frontend files
│   │   ├── 📁 css/              # Stylesheets
│   │   ├── 📁 js/               # JavaScript files
│   │   └── *.html               # HTML pages
│   └── application.properties   # Configuration
├── 📁 database/                 # Database scripts
│   ├── schema.sql              # Database schema
│   └── migration_add_images.sql # Image support migration
├── 📁 uploads/                  # File uploads
└── 📄 README.md                 # This file
```

## 🎨 Design Patterns

This project demonstrates the implementation of **5 key design patterns**:

### 1. 🏭 Factory Method Pattern
- **Location**: `pattern/factory/`
- **Purpose**: Creates different notification types
- **Implementation**: `NotificationFactory` with Email, SMS, Push, In-App notifications

### 2. 🔒 Singleton Pattern
- **Location**: `pattern/singleton/EmailConfiguration.java`
- **Purpose**: Single email configuration instance
- **Features**: Thread-safe, prevents cloning/deserialization

### 3. 🎨 Decorator Pattern
- **Location**: `pattern/decorator/`
- **Purpose**: Dynamically adds features to property descriptions
- **Components**: Basic property + decorators (Premium Location, Luxury Amenities, Furnished)

### 4. 👀 Observer Pattern
- **Location**: `pattern/observer/`
- **Purpose**: Notifies observers when property status changes
- **Observers**: Email notifications, audit logs, market updates

### 5. 🎯 Strategy Pattern
- **Location**: `pattern/strategy/`
- **Purpose**: Different pricing calculation algorithms
- **Strategies**: Standard, Premium, Discount pricing

### Demo Endpoints
```bash
# Test all patterns
GET /api/design-patterns/demo?propertyId=1

# Test individual patterns
GET /api/design-patterns/demo/factory
GET /api/design-patterns/demo/singleton
GET /api/design-patterns/demo/decorator
GET /api/design-patterns/demo/observer?propertyId=1
GET /api/design-patterns/demo/strategy?propertyId=1
```

## 🔐 Demo Accounts

| Role | Username | Password | Description |
|------|----------|----------|-------------|
| 👑 **Admin** | `admin` | `admin123` | Full system access |
| 🏠 **Seller** | `seller` | `seller123` | Property management |
| 🛒 **Buyer** | `buyer` | `buyer123` | Property browsing and booking |
| 🤝 **Agent** | `agent` | `agent123` | Property agent access |

## 📚 API Documentation

### User Management
```http
POST   /api/users/register     # Register new user
POST   /api/users/login        # User login
GET    /api/users/{id}         # Get user by ID
PUT    /api/users/{id}         # Update user
```

### Property Management
```http
GET    /api/properties                    # List all properties (with filters)
POST   /api/properties                    # Create property
GET    /api/properties/{id}               # Get property details
PUT    /api/properties/{id}                # Update property
DELETE /api/properties/{id}                # Delete property
POST   /api/properties/upload             # Upload property images
```

### Booking Management
```http
POST   /api/bookings                      # Create booking
GET    /api/bookings/buyer/{buyerId}      # Get bookings by buyer
PUT    /api/bookings/{id}/status         # Update booking status
```

### Rental Management
```http
POST   /api/rentals                       # Create rental agreement
GET    /api/rentals/tenant/{tenantId}     # Get agreements by tenant
PATCH  /api/rentals/{id}/extend          # Extend agreement
GET    /api/rentals/expiring              # Get expiring agreements
```

### Inquiry Management
```http
POST   /api/inquiries                     # Create inquiry
GET    /api/inquiries/sender/{senderId}  # Get inquiries by sender
PATCH  /api/inquiries/{id}/status        # Update inquiry status
```

### Offer Management
```http
POST   /api/offers                        # Create offer
POST   /api/offers/{id}/accept           # Accept offer
POST   /api/offers/{id}/counter          # Counter offer
GET    /api/offers/buyer/{buyerId}      # Get offers by buyer
```

## 🖼️ Screenshots

### Homepage
![Homepage](https://github.com/user-attachments/assets/49d020f3-dd5c-4156-9bba-21e9648a34e7)


### Property Listings
![Property Listings](https://github.com/user-attachments/assets/73e63b6d-a266-48e9-a72c-9bec1295e150)


### Dashboard
![Dashboard](https://github.com/user-attachments/assets/a6014c01-9f22-4a46-8b14-f1fe29b9d792)


### Booking System
![Booking System](https://github.com/user-attachments/assets/b5802aed-f3ec-4b5c-ac45-43ab4225e839)


## 🎓 Educational Value

This project serves as an excellent example of:

- **Enterprise Java Development** with Spring Boot
- **Design Pattern Implementation** in real-world scenarios
- **Full-Stack Web Development** with modern technologies
- **Database Design** with proper relationships
- **RESTful API Development** with comprehensive endpoints
- **Security Implementation** with Spring Security
- **Modern Frontend Development** with responsive design

## 🔧 Development Setup

### IDE Configuration
- **IntelliJ IDEA** (recommended) or **Eclipse**
- **Spring Boot** plugin for IDE
- **MySQL Workbench** for database management

### Code Style
- Follow **Java naming conventions**
- Use **Spring Boot best practices**
- Implement **proper error handling**
- Write **comprehensive comments**

## 🚀 Future Enhancements

- [ ] **Email Notifications** - Automated email system
- [ ] **File Upload** - Property image management
- [ ] **Advanced Search** - Map integration and filters
- [ ] **Payment Integration** - Online payment processing
- [ ] **Mobile App** - React Native or Flutter
- [ ] **Real-time Chat** - WebSocket implementation
- [ ] **Analytics Dashboard** - Property statistics
- [ ] **API Documentation** - Swagger/OpenAPI integration

## 👥 Team

**Project Group 2025-Y2-S1-MLB-B7G2-01**

| Name | IT Number |
|------|----------|
| **Pathirana P.M.K.H** | **IT24101797** |
| **Wathuthanthiri W.M.R.V** | **IT24101677** |
| **Maddegoda M.V.S** | **IT24101739** |
| **Dissanayake D.M.N.T** | **IT24102280** |
| **Gunasinghe N.A.L** | **IT24101721** |

- **Backend Development**: Java Spring Boot, MySQL, REST APIs
- **Frontend Development**: HTML5, CSS3, JavaScript ES6+
- **Database Design**: MySQL schema and relationships
- **Design Patterns**: Implementation of 5 key patterns
- **Documentation**: Comprehensive project documentation

## 📄 License

This project is developed as a **university assignment** and is for **educational purposes**.

---

<div align="center">

**🏠 Property Management System**  
*Built with ❤️ for University Project*

[![GitHub](https://img.shields.io/badge/GitHub-Repository-black?style=for-the-badge&logo=github)](https://github.com)
[![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green?style=for-the-badge&logo=spring)](https://spring.io/projects/spring-boot)

</div>
