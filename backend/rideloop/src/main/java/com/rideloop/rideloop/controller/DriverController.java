
package com.rideloop.rideloop.controller;

import com.rideloop.rideloop.entity.Driver;
import com.rideloop.rideloop.service.DriverService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@CrossOrigin(origins = "*")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    public Driver registerDriver(@RequestBody Driver driver) {
        return driverService.registerDriver(driver);
    }

    @GetMapping
    public List<Driver> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @GetMapping("/{id}")
    public Driver getDriverById(@PathVariable Long id) {
        return driverService.getDriverById(id);
    }

    // Update driver price adjustment
    @PutMapping("/{id}/price-adjustment")
    public Driver updatePriceAdjustment(
            @PathVariable Long id,
            @RequestParam double adjustment) {

        return driverService.updatePriceAdjustment(id, adjustment);
    }
}
