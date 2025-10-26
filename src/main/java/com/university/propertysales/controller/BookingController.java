package com.university.propertysales.controller;

import com.university.propertysales.dto.BookingCreateDTO;
import com.university.propertysales.dto.BookingDTO;
import com.university.propertysales.entity.Booking;
import com.university.propertysales.entity.Property;
import com.university.propertysales.entity.User;
import com.university.propertysales.mapper.BookingMapper;
import com.university.propertysales.service.EmailService;
import com.university.propertysales.service.PropertyService;
import com.university.propertysales.service.UserService;
import com.university.propertysales.repository.BookingRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingDTO> bookingDTOs = bookings.stream()
                .map(bookingMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookingDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isPresent()) {
            BookingDTO bookingDTO = bookingMapper.toDTO(booking.get());
            return ResponseEntity.ok(bookingDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingCreateDTO bookingCreateDTO) {
        try {
            Optional<Property> property = propertyService.getPropertyById(bookingCreateDTO.getPropertyId());
            Optional<User> buyer = userService.getUserById(bookingCreateDTO.getBuyerId());

            if (!property.isPresent()) {
                return ResponseEntity.badRequest().body("Property not found");
            }
            if (!buyer.isPresent()) {
                return ResponseEntity.badRequest().body("Buyer not found");
            }

            Booking booking = bookingMapper.toEntity(bookingCreateDTO, property.get(), buyer.get());
            Booking createdBooking = bookingRepository.save(booking);

            // Send email notifications
            try {
                emailService.sendBookingCreatedEmail(
                    buyer.get().getEmail(),
                    property.get().getSeller().getEmail(),
                    property.get().getTitle(),
                    createdBooking.getId()
                );
            } catch (Exception e) {
                System.err.println("Failed to send booking creation emails: " + e.getMessage());
            }

            BookingDTO bookingDTO = bookingMapper.toDTO(createdBooking);
            return ResponseEntity.status(HttpStatus.CREATED).body(bookingDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @Valid @RequestBody BookingCreateDTO bookingUpdateDTO) {
        try {
            Optional<Booking> bookingOpt = bookingRepository.findById(id);
            if (!bookingOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Booking booking = bookingOpt.get();
            
            // Update booking details
            Optional<Property> property = propertyService.getPropertyById(bookingUpdateDTO.getPropertyId());
            Optional<User> buyer = userService.getUserById(bookingUpdateDTO.getBuyerId());

            if (!property.isPresent()) {
                return ResponseEntity.badRequest().body("Property not found");
            }
            if (!buyer.isPresent()) {
                return ResponseEntity.badRequest().body("Buyer not found");
            }

            booking.setProperty(property.get());
            booking.setBuyer(buyer.get());
            booking.setBookingDate(bookingUpdateDTO.getBookingDate());
            booking.setBookingTime(bookingUpdateDTO.getBookingTime());

            Booking updatedBooking = bookingRepository.save(booking);
            BookingDTO bookingDTO = bookingMapper.toDTO(updatedBooking);

            return ResponseEntity.ok(bookingDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest request) {
        try {
            Optional<Booking> bookingOpt = bookingRepository.findById(id);
            if (!bookingOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Booking booking = bookingOpt.get();
            booking.setStatus(Booking.BookingStatus.valueOf(request.getStatus().toUpperCase()));
            Booking updatedBooking = bookingRepository.save(booking);

            // Send email notifications
            try {
                emailService.sendBookingStatusUpdateEmail(
                    updatedBooking.getBuyer().getEmail(),
                    updatedBooking.getProperty().getSeller().getEmail(),
                    updatedBooking.getProperty().getTitle(),
                    request.getStatus(),
                    updatedBooking.getId()
                );
            } catch (Exception e) {
                System.err.println("Failed to send booking status update emails: " + e.getMessage());
            }

            BookingDTO bookingDTO = bookingMapper.toDTO(updatedBooking);
            return ResponseEntity.ok(bookingDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<?> getBookingsByProperty(@PathVariable Long propertyId) {
        Optional<Property> property = propertyService.getPropertyById(propertyId);
        if (!property.isPresent()) {
            return ResponseEntity.badRequest().body("Property not found");
        }

        List<Booking> bookings = bookingRepository.findByProperty(property.get());
        List<BookingDTO> bookingDTOs = bookings.stream()
                .map(bookingMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(bookingDTOs);
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<?> getBookingsByBuyer(@PathVariable Long buyerId) {
        Optional<User> buyer = userService.getUserById(buyerId);
        if (!buyer.isPresent()) {
            return ResponseEntity.badRequest().body("Buyer not found");
        }

        List<Booking> bookings = bookingRepository.findByBuyer(buyer.get());
        List<BookingDTO> bookingDTOs = bookings.stream()
                .map(bookingMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(bookingDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        try {
            if (!bookingRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            bookingRepository.deleteById(id);
            return ResponseEntity.ok().body("Booking deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Inner class for status update request
    public static class StatusUpdateRequest {
        private String status;

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
