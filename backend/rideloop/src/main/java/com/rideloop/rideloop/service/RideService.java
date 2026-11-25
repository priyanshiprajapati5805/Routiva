
package com.rideloop.rideloop.service;

import com.rideloop.rideloop.entity.Ride;
import com.rideloop.rideloop.entity.Driver;
import com.rideloop.rideloop.exception.RideNotFoundException;
import com.rideloop.rideloop.repository.RideRepository;
import com.rideloop.rideloop.repository.DriverRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;

    public RideService(RideRepository rideRepository,
                       DriverRepository driverRepository) {
        this.rideRepository = rideRepository;
        this.driverRepository = driverRepository;
    }

    // CREATE RIDE WITH HYBRID PRICING
    public Ride createRide(Ride ride) {

        if (ride.getDriver() == null ||
            ride.getDriver().getId() == null) {
            throw new RuntimeException("Driver ID is required!");
        }

        Long driverId = ride.getDriver().getId();

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() ->
                    new RuntimeException("Driver not found!")
                );

        // Calculate route base price
        double basePrice = getBasePrice(
                ride.getPickupLocation(),
                ride.getDropLocation()
        );

        // Driver-wise adjustment
        double adjustment = driver.getPriceAdjustment();

        if (adjustment < 0) {
            throw new RuntimeException(
                "Driver price adjustment cannot be negative!"
            );
        }

        // Final price = Base price + Driver adjustment
        double finalPrice = basePrice + adjustment;

        ride.setDriver(driver);
        ride.setPricePerSeat(finalPrice);

        if (ride.getRideStatus() == null ||
            ride.getRideStatus().isBlank()) {
            ride.setRideStatus("AVAILABLE");
        }

        if (ride.getAvailableSeats() <= 0) {
            ride.setAvailableSeats(ride.getTotalSeats());
        }

        return rideRepository.save(ride);
    }

    // ROUTE BASE PRICE
    private double getBasePrice(String pickup, String drop) {

        if (pickup == null || drop == null) {
            throw new RuntimeException(
                "Pickup and drop locations are required!"
            );
        }

        String from = pickup.trim().toLowerCase();
        String to = drop.trim().toLowerCase();

        if (from.equals(to)) {
            throw new RuntimeException(
                "Pickup and drop locations cannot be same!"
            );
        }

        if (isRoute(from, to, "ahmedabad", "gandhinagar")) {
            return 150;
        }

        if (isRoute(from, to, "ahmedabad", "bavla")) {
            return 100;
        }

        if (isRoute(from, to, "ahmedabad", "sanand")) {
            return 120;
        }

        if (isRoute(from, to, "ahmedabad", "vadodara")) {
            return 300;
        }

        if (isRoute(from, to, "ahmedabad", "surat")) {
            return 600;
        }

        throw new RuntimeException(
            "Pricing is not available for this route yet!"
        );
    }

    // Check both directions of a route
    private boolean isRoute(String from, String to,
                            String city1, String city2) {

        return (from.equals(city1) && to.equals(city2)) ||
               (from.equals(city2) && to.equals(city1));
    }

    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    public Ride getRideById(Long id) {
        return rideRepository.findById(id)
                .orElseThrow(() ->
                    new RideNotFoundException(
                        "Ride not found with ID: " + id
                    )
                );
    }

    // SEARCH RIDES BY LOCATION, DATE AND SEATS
    public List<Ride> searchRides(
            String pickupLocation,
            String dropLocation,
            LocalDate date,
            int requiredSeats) {

        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.plusDays(1).atStartOfDay();

        return rideRepository
                .findByPickupLocationIgnoreCaseAndDropLocationIgnoreCaseAndRideDateTimeBetweenAndAvailableSeatsGreaterThanEqualAndRideStatus(
                        pickupLocation,
                        dropLocation,
                        startDate,
                        endDate,
                        requiredSeats,
                        "AVAILABLE"
                );
    }

    public void deleteRide(Long id) {
        Ride ride = getRideById(id);
        rideRepository.delete(ride);
    }
}
