
package com.rideloop.rideloop.service;

import com.rideloop.rideloop.entity.Passenger;
import com.rideloop.rideloop.repository.PassengerRepository;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PassengerService {

    private final PassengerRepository passengerRepository;

    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    public Passenger registerPassenger(Passenger passenger) {

        List<Passenger> existingPassengers =
                passengerRepository.findAllByEmail(passenger.getEmail());

        if (!existingPassengers.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Email already registered! Please use another email."
            );
        }

        return passengerRepository.save(passenger);
    }

    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    public Passenger getPassengerById(Long id) {
        return passengerRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Passenger not found!")
                );
    }

    public Optional<Passenger> loginPassenger(String email, String password) {

        Optional<Passenger> passenger =
                passengerRepository.findByEmail(email);

        if (passenger.isPresent() &&
                passenger.get().getPassword().equals(password)) {

            return passenger;
        }

        return Optional.empty();
    }
}
