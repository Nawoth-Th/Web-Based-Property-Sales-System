package com.university.propertysales.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            
            mailSender.send(message);
            System.out.println("Email sent successfully to: " + to);
        } catch (Exception e) {
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
        }
    }

    // Rental Agreement Email Templates
    public void sendAgreementCreatedEmail(String tenantEmail, String landlordEmail, String propertyTitle, Long agreementId) {
        String subject = "New Rental Agreement Created - PropertyHub";
        String tenantText = String.format(
            "Dear Tenant,\n\n" +
            "A new rental agreement has been created for property: %s\n" +
            "Agreement ID: %d\n\n" +
            "Please review the agreement details in your PropertyHub dashboard.\n\n" +
            "Best regards,\nProperty Management System Team",
            propertyTitle, agreementId
        );
        
        String landlordText = String.format(
            "Dear Landlord,\n\n" +
            "A new rental agreement has been created for your property: %s\n" +
            "Agreement ID: %d\n\n" +
            "Please review the agreement details in your PropertyHub dashboard.\n\n" +
            "Best regards,\nProperty Management System Team",
            propertyTitle, agreementId
        );

        sendSimpleEmail(tenantEmail, subject, tenantText);
        sendSimpleEmail(landlordEmail, subject, landlordText);
    }

    public void sendAgreementStatusUpdateEmail(String tenantEmail, String landlordEmail, String propertyTitle, String status, Long agreementId) {
        String subject = "Rental Agreement Status Updated - PropertyHub";
        String text = String.format(
            "Dear User,\n\n" +
            "The rental agreement for property '%s' has been updated.\n" +
            "Agreement ID: %d\n" +
            "New Status: %s\n\n" +
            "Please check your PropertyHub dashboard for more details.\n\n" +
            "Best regards,\nProperty Management System Team",
            propertyTitle, agreementId, status
        );

        sendSimpleEmail(tenantEmail, subject, text);
        sendSimpleEmail(landlordEmail, subject, text);
    }

    // Booking Email Templates
    public void sendBookingCreatedEmail(String buyerEmail, String sellerEmail, String propertyTitle, Long bookingId) {
        String subject = "New Property Booking Created - PropertyHub";
        String buyerText = String.format(
            "Dear Buyer,\n\n" +
            "Your booking has been created for property: %s\n" +
            "Booking ID: %d\n\n" +
            "Please check your PropertyHub dashboard for booking details.\n\n" +
            "Best regards,\nProperty Management System Team",
            propertyTitle, bookingId
        );
        
        String sellerText = String.format(
            "Dear Seller,\n\n" +
            "A new booking has been created for your property: %s\n" +
            "Booking ID: %d\n\n" +
            "Please check your PropertyHub dashboard for booking details.\n\n" +
            "Best regards,\nProperty Management System Team",
            propertyTitle, bookingId
        );

        sendSimpleEmail(buyerEmail, subject, buyerText);
        sendSimpleEmail(sellerEmail, subject, sellerText);
    }

    public void sendBookingStatusUpdateEmail(String buyerEmail, String sellerEmail, String propertyTitle, String status, Long bookingId) {
        String subject = "Property Booking Status Updated - PropertyHub";
        String text = String.format(
            "Dear User,\n\n" +
            "The booking for property '%s' has been updated.\n" +
            "Booking ID: %d\n" +
            "New Status: %s\n\n" +
            "Please check your PropertyHub dashboard for more details.\n\n" +
            "Best regards,\nProperty Management System Team",
            propertyTitle, bookingId, status
        );

        sendSimpleEmail(buyerEmail, subject, text);
        sendSimpleEmail(sellerEmail, subject, text);
    }

    // Inquiry Email Templates
    public void sendInquiryCreatedEmail(String senderEmail, String propertyOwnerEmail, String propertyTitle, Long inquiryId) {
        String subject = "New Property Inquiry - PropertyHub";
        String senderText = String.format(
            "Dear User,\n\n" +
            "Your inquiry has been submitted for property: %s\n" +
            "Inquiry ID: %d\n\n" +
            "The property owner will be notified and should respond soon.\n\n" +
            "Best regards,\nProperty Management System Team",
            propertyTitle, inquiryId
        );
        
        String ownerText = String.format(
            "Dear Property Owner,\n\n" +
            "A new inquiry has been received for your property: %s\n" +
            "Inquiry ID: %d\n\n" +
            "Please check your PropertyHub dashboard to respond.\n\n" +
            "Best regards,\nProperty Management System Team",
            propertyTitle, inquiryId
        );

        sendSimpleEmail(senderEmail, subject, senderText);
        sendSimpleEmail(propertyOwnerEmail, subject, ownerText);
    }

    public void sendInquiryStatusUpdateEmail(String senderEmail, String propertyOwnerEmail, String propertyTitle, String status, Long inquiryId) {
        String subject = "Property Inquiry Status Updated - PropertyHub";
        String text = String.format(
            "Dear User,\n\n" +
            "The inquiry for property '%s' has been updated.\n" +
            "Inquiry ID: %d\n" +
            "New Status: %s\n\n" +
            "Please check your PropertyHub dashboard for more details.\n\n" +
            "Best regards,\nProperty Management System Team",
            propertyTitle, inquiryId, status
        );

        sendSimpleEmail(senderEmail, subject, text);
        sendSimpleEmail(propertyOwnerEmail, subject, text);
    }

    // Offer Email Templates
    public void sendOfferCreatedEmail(String buyerEmail, String sellerEmail, String propertyTitle, Long offerId) {
        String subject = "New Property Offer - PropertyHub";
        String buyerText = String.format(
            "Dear Buyer,\n\n" +
            "Your offer has been submitted for property: %s\n" +
            "Offer ID: %d\n\n" +
            "The property owner will be notified and should respond soon.\n\n" +
            "Best regards,\nProperty Management System Team",
            propertyTitle, offerId
        );
        
        String sellerText = String.format(
            "Dear Property Owner,\n\n" +
            "A new offer has been received for your property: %s\n" +
            "Offer ID: %d\n\n" +
            "Please check your PropertyHub dashboard to review and respond.\n\n" +
            "Best regards,\nProperty Management System Team",
            propertyTitle, offerId
        );

        sendSimpleEmail(buyerEmail, subject, buyerText);
        sendSimpleEmail(sellerEmail, subject, sellerText);
    }

    public void sendOfferStatusUpdateEmail(String buyerEmail, String sellerEmail, String propertyTitle, String status, Long offerId) {
        String subject = "Property Offer Status Updated - PropertyHub";
        String text = String.format(
            "Dear User,\n\n" +
            "The offer for property '%s' has been updated.\n" +
            "Offer ID: %d\n" +
            "New Status: %s\n\n" +
            "Please check your PropertyHub dashboard for more details.\n\n" +
            "Best regards,\nProperty Management System Team",
            propertyTitle, offerId, status
        );

        sendSimpleEmail(buyerEmail, subject, text);
        sendSimpleEmail(sellerEmail, subject, text);
    }
}
