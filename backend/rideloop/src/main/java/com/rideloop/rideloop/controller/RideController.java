
package com.rideloop.rideloop.controller;

import com.rideloop.rideloop.entity.Ride;
import com.rideloop.rideloop.service.RideService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/rides")
@CrossOrigin(origins = "*")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    // Create a new ride
    @PostMapping
    public Ride createRide(@RequestBody Ride ride) {
        return rideService.createRide(ride);
    }

    // Get all rides
    @GetMapping
    public List<Ride> getAllRides() {
        return rideService.getAllRides();
    }

    // Get ride by ID
    @GetMapping("/{id}")
    public Ride getRideById(@PathVariable Long id) {
        return rideService.getRideById(id);
    }

    // Search rides
    @GetMapping("/search")
    public List<Ride> searchRides(
            @RequestParam String pickupLocation,
            @RequestParam String dropLocation,
            @RequestParam String date,
            @RequestParam int seats) {

        LocalDate travelDate = LocalDate.parse(date);

        return rideService.searchRides(
                pickupLocation,
                dropLocation,
                travelDate,
                seats
        );
    }

    // Delete ride
    @DeleteMapping("/{id}")
    public String deleteRide(@PathVariable Long id) {
        rideService.deleteRide(id);
        return "Ride deleted successfully!";
    }
}

