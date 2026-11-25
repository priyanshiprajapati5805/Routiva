
package com.rideloop.rideloop.repository;

import com.rideloop.rideloop.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository
        extends JpaRepository<Driver, Long> {
}
