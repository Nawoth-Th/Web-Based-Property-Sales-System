package com.university.propertysales.repository;

import com.university.propertysales.entity.Booking;
import com.university.propertysales.entity.Property;
import com.university.propertysales.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByProperty(Property property);
    List<Booking> findByBuyer(User buyer);
    List<Booking> findByStatus(Booking.BookingStatus status);
    List<Booking> findByBookingDate(LocalDate bookingDate);
    List<Booking> findByPropertyAndBookingDate(Property property, LocalDate bookingDate);
}
