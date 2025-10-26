package com.university.propertysales.service;

import com.university.propertysales.entity.Booking;
import com.university.propertysales.entity.Property;
import com.university.propertysales.entity.User;
import com.university.propertysales.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;

    public Booking createBooking(Booking booking) {
        // Check for conflicting bookings
        List<Booking> existingBookings = bookingRepository.findByPropertyAndBookingDate(
            booking.getProperty(), booking.getBookingDate());
        
        for (Booking existing : existingBookings) {
            if (existing.getBookingTime().equals(booking.getBookingTime()) && 
                existing.getStatus() != Booking.BookingStatus.CANCELLED) {
                throw new RuntimeException("Time slot already booked");
            }
        }
        
        return bookingRepository.save(booking);
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByProperty(Property property) {
        return bookingRepository.findByProperty(property);
    }

    public List<Booking> getBookingsByBuyer(User buyer) {
        return bookingRepository.findByBuyer(buyer);
    }

    public List<Booking> getBookingsByStatus(Booking.BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

    public List<Booking> getBookingsByDate(LocalDate date) {
        return bookingRepository.findByBookingDate(date);
    }

    public Booking updateBookingStatus(Long id, Booking.BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Long id, Booking bookingDetails) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        booking.setBookingDate(bookingDetails.getBookingDate());
        booking.setBookingTime(bookingDetails.getBookingTime());
        booking.setStatus(bookingDetails.getStatus());
        
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found");
        }
        bookingRepository.deleteById(id);
    }

    public List<Booking> getAvailableTimeSlots(Property property, LocalDate date) {
        return bookingRepository.findByPropertyAndBookingDate(property, date);
    }
}
