
package com.rideloop.rideloop.repository;

import com.rideloop.rideloop.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;


@Repository
public interface PassengerRepository
        extends JpaRepository<Passenger, Long> {

    Optional<Passenger> findByEmail(String email);
    List<Passenger> findAllByEmail(String email);
}
