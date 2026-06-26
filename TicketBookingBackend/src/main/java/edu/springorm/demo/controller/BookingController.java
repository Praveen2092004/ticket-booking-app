package edu.springorm.demo.controller;

import edu.springorm.demo.model.Booking;
import edu.springorm.demo.repository.BookingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
// ✅ Added explicit CORS permission for ports 3000, 3001, and 3002
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:3002"})
public class BookingController {

    private final BookingRepository repository;

    @Autowired
    public BookingController(BookingRepository repository) {
        this.repository = repository;
    }

    // 1. GET Endpoint - Fetch all booking data records
    @GetMapping("/all")
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = repository.findAll();
        return ResponseEntity.ok(bookings);
    }

    // 2. POST Endpoint - Receive and process form submission details from React
    @PostMapping("/add")
    public ResponseEntity<Booking> bookTicket(@RequestBody Booking booking) {

        if (booking == null || booking.getTickets() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Set calculation based on tickets count
        double totalAmount = booking.getTickets() * 150.0;
        booking.setTotalAmount(totalAmount);

        // Commit tracking record directly into your Aiven MySQL cloud database instance
        Booking savedBooking = repository.save(booking);

        return ResponseEntity.ok(savedBooking);
    }
}