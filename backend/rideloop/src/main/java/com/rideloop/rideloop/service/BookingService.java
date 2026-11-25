
package com.rideloop.rideloop.service;

import com.rideloop.rideloop.entity.Booking;
import com.rideloop.rideloop.entity.Ride;
import com.rideloop.rideloop.entity.Passenger;

import com.rideloop.rideloop.exception.RideNotFoundException;
import com.rideloop.rideloop.exception.InsufficientSeatsException;

import com.rideloop.rideloop.repository.BookingRepository;
import com.rideloop.rideloop.repository.RideRepository;
import com.rideloop.rideloop.repository.PassengerRepository;

import com.rideloop.rideloop.notification.RideNotificationService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RideRepository rideRepository;
    private final PassengerRepository passengerRepository;
    private final RideNotificationService notificationService;

    public BookingService(
            BookingRepository bookingRepository,
            RideRepository rideRepository,
            PassengerRepository passengerRepository,
            RideNotificationService notificationService) {

        this.bookingRepository = bookingRepository;
        this.rideRepository = rideRepository;
        this.passengerRepository = passengerRepository;
        this.notificationService = notificationService;
    }

    // Create Booking
    @Transactional
    public Booking createBooking(Long rideId,
                                 Long passengerId,
                                 int numberOfSeats) {

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() ->
                    new RideNotFoundException("Ride not found!")
                );

        Passenger passenger = passengerRepository
                .findById(passengerId)
                .orElseThrow(() ->
                    new RuntimeException("Passenger not found!")
                );

        if (numberOfSeats <= 0) {
            throw new IllegalArgumentException(
                "Number of seats must be greater than zero!"
            );
        }

        if (ride.getAvailableSeats() < numberOfSeats) {
            throw new InsufficientSeatsException(
                "Not enough seats available!"
            );
        }

        // Calculate total amount
        double totalAmount =
                ride.getPricePerSeat() * numberOfSeats;

        // Update available seats
        ride.setAvailableSeats(
                ride.getAvailableSeats() - numberOfSeats
        );

        rideRepository.save(ride);

        // Create booking
        Booking booking = new Booking(
                LocalDateTime.now(),
                numberOfSeats,
                totalAmount,
                "CONFIRMED",
                passenger,
                ride
        );

        Booking savedBooking = bookingRepository.save(booking);

        // Send background notification
        notificationService.sendNotification(
                ride.getId(),
                passenger.getName(),
                ride.getDriver().getName()
        );

        return savedBooking;
    }

    // Get All Bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Get Booking By ID
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Booking not found!")
                );
    }

    // Get Bookings By Passenger ID
    public List<Booking> getBookingsByPassengerId(Long passengerId) {
        return bookingRepository.findByPassengerId(passengerId);
    }

    // Cancel Booking
    @Transactional
    public Booking cancelBooking(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Booking not found!")
                );

        // Check whether booking is already cancelled
        if ("CANCELLED".equalsIgnoreCase(booking.getBookingStatus())) {
            throw new IllegalStateException(
                "Booking is already cancelled!"
            );
        }

        // Get ride
        Ride ride = booking.getRide();

        // Return booked seats
        ride.setAvailableSeats(
                ride.getAvailableSeats() + booking.getNumberOfSeats()
        );

        rideRepository.save(ride);

        // Update booking status
        booking.setBookingStatus("CANCELLED");

        return bookingRepository.save(booking);
    }
}
