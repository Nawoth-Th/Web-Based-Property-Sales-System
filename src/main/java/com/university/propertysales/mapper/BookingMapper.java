package com.university.propertysales.mapper;

import com.university.propertysales.dto.BookingCreateDTO;
import com.university.propertysales.dto.BookingDTO;
import com.university.propertysales.entity.Booking;
import com.university.propertysales.entity.Property;
import com.university.propertysales.entity.User;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingDTO toDTO(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());

        if (booking.getProperty() != null) {
            dto.setPropertyId(booking.getProperty().getId());
            dto.setPropertyTitle(booking.getProperty().getTitle());
        }

        if (booking.getBuyer() != null) {
            dto.setBuyerId(booking.getBuyer().getId());
            dto.setBuyerUsername(booking.getBuyer().getUsername());
        }

        dto.setBookingDate(booking.getBookingDate());
        dto.setBookingTime(booking.getBookingTime());
        dto.setStatus(booking.getStatus() != null ? booking.getStatus().name() : null);
        dto.setCreatedAt(booking.getCreatedAt());
        dto.setUpdatedAt(booking.getUpdatedAt());

        return dto;
    }

    public Booking toEntity(BookingCreateDTO dto, Property property, User buyer) {
        if (dto == null) {
            return null;
        }

        Booking booking = new Booking();
        booking.setProperty(property);
        booking.setBuyer(buyer);
        booking.setBookingDate(dto.getBookingDate());
        booking.setBookingTime(dto.getBookingTime());

        return booking;
    }
}
