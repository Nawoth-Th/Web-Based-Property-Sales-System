package com.university.propertysales.config;

import com.university.propertysales.entity.User;
import com.university.propertysales.entity.Property;
import com.university.propertysales.entity.Offer;
import com.university.propertysales.entity.Inquiry;
import com.university.propertysales.entity.RentalAgreement;
import com.university.propertysales.repository.UserRepository;
import com.university.propertysales.repository.PropertyRepository;
import com.university.propertysales.repository.OfferRepository;
import com.university.propertysales.repository.InquiryRepository;
import com.university.propertysales.repository.RentalAgreementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private RentalAgreementRepository rentalAgreementRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            initializeDemoUsers();
            initializeDemoProperties();
        }
        if (offerRepository.count() == 0) {
            initializeDemoOffers();
        }
        if (inquiryRepository.count() == 0) {
            initializeDemoInquiries();
        }
        if (rentalAgreementRepository.count() == 0) {
            initializeDemoRentalAgreements();
        }
    }

    private void initializeDemoUsers() {
        // Create demo users in database to match Spring Security users
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@propertyhub.demo");
        admin.setRole(User.Role.ADMIN);
        userRepository.save(admin);

        User seller = new User();
        seller.setUsername("seller");
        seller.setPassword(passwordEncoder.encode("seller123"));
        seller.setEmail("seller@propertyhub.demo");
        seller.setRole(User.Role.SELLER);
        userRepository.save(seller);

        User buyer = new User();
        buyer.setUsername("buyer");
        buyer.setPassword(passwordEncoder.encode("buyer123"));
        buyer.setEmail("buyer@propertyhub.demo");
        buyer.setRole(User.Role.BUYER);
        userRepository.save(buyer);

        User agent = new User();
        agent.setUsername("agent");
        agent.setPassword(passwordEncoder.encode("agent123"));
        agent.setEmail("agent@propertyhub.demo");
        agent.setRole(User.Role.AGENT);
        userRepository.save(agent);

        System.out.println("Demo users initialized successfully!");
    }

    private void initializeDemoProperties() {
        // Get the seller user for property ownership
        User seller = userRepository.findByUsername("seller").orElse(null);
        User agent = userRepository.findByUsername("agent").orElse(null);

        if (seller != null) {
            Property property1 = new Property();
            property1.setTitle("Modern 3BR Apartment");
            property1.setType(Property.PropertyType.SALE);
            property1.setLocation("Downtown City Center");
            property1.setPrice(new BigDecimal("45000000.00")); // Rs 45,000,000 (approx $450,000)
            property1.setDescription("Beautiful modern apartment with city views, hardwood floors, and updated kitchen");
            property1.setStatus(Property.PropertyStatus.AVAILABLE);
            property1.setSeller(seller);
            property1.setMainImage("/uploads/properties/5fb09bc2-4896-4a97-b064-fd6658c8a1ba.jpg");
            property1.setImages("/uploads/properties/5fb09bc2-4896-4a97-b064-fd6658c8a1ba.jpg");
            propertyRepository.save(property1);

            Property property2 = new Property();
            property2.setTitle("Cozy 2BR House");
            property2.setType(Property.PropertyType.RENT);
            property2.setLocation("Suburban Area");
            property2.setRentAmount(new BigDecimal("250000.00")); // Rs 250,000 (approx $2,500)
            property2.setDescription("Perfect family home with garden, garage, and quiet neighborhood");
            property2.setStatus(Property.PropertyStatus.AVAILABLE);
            property2.setSeller(seller);
            // Add placeholder image URL for demo purposes
            property2.setMainImage("https://images.unsplash.com/photo-1564013799919-ab600027ffc6?w=400&h=300&fit=crop");
            property2.setImages("https://images.unsplash.com/photo-1564013799919-ab600027ffc6?w=400&h=300&fit=crop");
            propertyRepository.save(property2);
        }

        if (agent != null) {
            Property property3 = new Property();
            property3.setTitle("Luxury Villa");
            property3.setType(Property.PropertyType.SALE);
            property3.setLocation("Premium District");
            property3.setPrice(new BigDecimal("1200000.00"));
            property3.setDescription("Exclusive villa with pool, garage, and premium finishes");
            property3.setStatus(Property.PropertyStatus.AVAILABLE);
            property3.setSeller(agent);
            // Add placeholder image URL for demo purposes
            property3.setMainImage("https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?w=400&h=300&fit=crop");
            property3.setImages("https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?w=400&h=300&fit=crop");
            propertyRepository.save(property3);

            Property property4 = new Property();
            property4.setTitle("Studio Apartment");
            property4.setType(Property.PropertyType.RENT);
            property4.setLocation("City Center");
            property4.setRentAmount(new BigDecimal("1800.00"));
            property4.setDescription("Modern studio in the heart of the city with all amenities");
            property4.setStatus(Property.PropertyStatus.AVAILABLE);
            property4.setSeller(agent);
            // Add placeholder image URL for demo purposes
            property4.setMainImage("https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=400&h=300&fit=crop");
            property4.setImages("https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=400&h=300&fit=crop");
            propertyRepository.save(property4);
        }

        System.out.println("Demo properties initialized successfully!");
    }

    private void initializeDemoOffers() {
        // Get users and properties for offers
        User buyer = userRepository.findByUsername("buyer").orElse(null);
        User seller = userRepository.findByUsername("seller").orElse(null);
        User agent = userRepository.findByUsername("agent").orElse(null);

        if (buyer != null && seller != null) {
            // Get properties to create offers for
            Property property1 = propertyRepository.findByTitle("Modern 3BR Apartment").orElse(null);
            Property property2 = propertyRepository.findByTitle("Cozy 2BR House").orElse(null);

            if (property1 != null) {
                // Create offer for Modern 3BR Apartment
                Offer offer1 = new Offer();
                offer1.setProperty(property1);
                offer1.setBuyer(buyer);
                offer1.setPrice(new BigDecimal("35000000.00")); // Rs 35,000,000 (below asking price)
                offer1.setTerms("Looking to purchase this property. Can close within 30 days.");
                offer1.setStatus(Offer.OfferStatus.ACCEPTED);
                offerRepository.save(offer1);
            }

            if (property2 != null) {
                // Create offer for Cozy 2BR House
                Offer offer2 = new Offer();
                offer2.setProperty(property2);
                offer2.setBuyer(buyer);
                offer2.setPrice(new BigDecimal("400000.00")); // Rs 400,000 (above asking price)
                offer2.setTerms("Interested in renting this property. Can move in immediately.");
                offer2.setStatus(Offer.OfferStatus.PENDING);
                offerRepository.save(offer2);
            }
        }

        System.out.println("Demo offers initialized successfully!");
    }

    private void initializeDemoInquiries() {
        // Get users and properties for inquiries
        User buyer = userRepository.findByUsername("buyer").orElse(null);
        User agent = userRepository.findByUsername("agent").orElse(null);

        if (buyer != null && agent != null) {
            // Get properties to create inquiries for
            Property property1 = propertyRepository.findByTitle("Modern 3BR Apartment").orElse(null);
            Property property2 = propertyRepository.findByTitle("Luxury Villa").orElse(null);

            if (property1 != null) {
                // Create inquiry for Modern 3BR Apartment
                Inquiry inquiry1 = new Inquiry();
                inquiry1.setProperty(property1);
                inquiry1.setSender(buyer);
                inquiry1.setMessage("I'm interested in this property. Can I schedule a viewing?");
                inquiry1.setStatus(Inquiry.InquiryStatus.RESOLVED);
                inquiryRepository.save(inquiry1);
            }

            if (property2 != null) {
                // Create inquiry for Luxury Villa
                Inquiry inquiry2 = new Inquiry();
                inquiry2.setProperty(property2);
                inquiry2.setSender(buyer);
                inquiry2.setMessage("Hello, I would like to know more about this villa. Is it still available?");
                inquiry2.setStatus(Inquiry.InquiryStatus.OPEN);
                inquiryRepository.save(inquiry2);
            }
        }

        System.out.println("Demo inquiries initialized successfully!");
    }

    private void initializeDemoRentalAgreements() {
        // Get users and properties for rental agreements
        User buyer = userRepository.findByUsername("buyer").orElse(null);
        User seller = userRepository.findByUsername("seller").orElse(null);
        User agent = userRepository.findByUsername("agent").orElse(null);

        if (buyer != null && seller != null) {
            // Get rental properties
            Property property1 = propertyRepository.findByTitle("Cozy 2BR House").orElse(null);
            Property property2 = propertyRepository.findByTitle("Studio Apartment").orElse(null);

            if (property1 != null) {
                // Create rental agreement for Cozy 2BR House
                RentalAgreement agreement1 = new RentalAgreement();
                agreement1.setProperty(property1);
                agreement1.setTenant(buyer);
                agreement1.setLandlord(seller);
                agreement1.setRent(new BigDecimal("60000.00")); // Rs 60,000/month
                agreement1.setDurationMonths(12);
                agreement1.setStartDate(LocalDate.now().minusMonths(6));
                agreement1.setStatus(RentalAgreement.AgreementStatus.TERMINATED);
                rentalAgreementRepository.save(agreement1);
            }

            if (property2 != null) {
                // Create rental agreement for Studio Apartment
                RentalAgreement agreement2 = new RentalAgreement();
                agreement2.setProperty(property2);
                agreement2.setTenant(buyer);
                agreement2.setLandlord(agent);
                agreement2.setRent(new BigDecimal("50000.00")); // Rs 50,000/month
                agreement2.setDurationMonths(6);
                agreement2.setStartDate(LocalDate.now().minusMonths(3));
                agreement2.setStatus(RentalAgreement.AgreementStatus.TERMINATED);
                rentalAgreementRepository.save(agreement2);
            }
        }

        System.out.println("Demo rental agreements initialized successfully!");
    }
}
