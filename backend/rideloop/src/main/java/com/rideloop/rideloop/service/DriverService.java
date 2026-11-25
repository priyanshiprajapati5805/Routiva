
package com.rideloop.rideloop.service;

import com.rideloop.rideloop.entity.Driver;
import com.rideloop.rideloop.repository.DriverRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    // Register Driver
    public Driver registerDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    // Get All Drivers
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    // Get Driver By ID
    public Driver getDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Driver not found!")
                );
    }

    // Update Driver Price Adjustment
    public Driver updatePriceAdjustment(
            Long id, double adjustment) {

        if (adjustment < 0) {
            throw new RuntimeException(
                "Price adjustment cannot be negative!"
            );
        }

        Driver driver = getDriverById(id);

        driver.setPriceAdjustment(adjustment);

        return driverRepository.save(driver);
    }
}
