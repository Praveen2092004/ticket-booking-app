package edu.springorm.demo.controller;

import edu.springorm.demo.model.Booking;
import edu.springorm.demo.repository.BookingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:3000") // Allows your React app to connect
public class BookingController {

    private final BookingRepository repository;

    // Constructor Injection
    @Autowired
    public BookingController(BookingRepository repository) {
        this.repository = repository;
    }

    // 1. GET Endpoint - To fetch data (Safe to test directly in your browser tab)
    @GetMapping("/all")
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = repository.findAll();
        return ResponseEntity.ok(bookings);
    }

    // 2. POST Endpoint - To receive data from your React form submission
    @PostMapping("/add")
    public ResponseEntity<Booking> bookTicket(@RequestBody Booking booking) {

        // Null safety check
        if (booking == null || booking.getTickets() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Business logic calculation
        double totalAmount = booking.getTickets() * 150.0;
        booking.setTotalAmount(totalAmount);

        // Save to your Aiven MySQL cloud instance
        Booking savedBooking = repository.save(booking);

        return ResponseEntity.ok(savedBooking);
    }
}