
package com.rideloop.rideloop.controller;

import com.rideloop.rideloop.entity.Booking;
import com.rideloop.rideloop.service.BookingService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Create Booking
    @PostMapping
    public Booking createBooking(
            @RequestParam Long rideId,
            @RequestParam Long passengerId,
            @RequestParam int numberOfSeats) {

        return bookingService.createBooking(
                rideId,
                passengerId,
                numberOfSeats
        );
    }

    // Get All Bookings
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    // Get Booking By ID
    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    // Get Bookings By Passenger ID
    @GetMapping("/passenger/{passengerId}")
    public List<Booking> getBookingsByPassengerId(
            @PathVariable Long passengerId) {

        return bookingService.getBookingsByPassengerId(passengerId);
    }

    // Cancel Booking
    @PutMapping("/{id}/cancel")
    public Booking cancelBooking(@PathVariable Long id) {
        return bookingService.cancelBooking(id);
    }
}
