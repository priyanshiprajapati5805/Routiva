
package com.rideloop.rideloop.repository;

import com.rideloop.rideloop.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    List<Ride> findByPickupLocationIgnoreCaseAndDropLocationIgnoreCaseAndRideDateTimeBetweenAndAvailableSeatsGreaterThanEqualAndRideStatus(
            String pickupLocation,
            String dropLocation,
            LocalDateTime startDate,
            LocalDateTime endDate,
            int requiredSeats,
            String rideStatus
    );
}

