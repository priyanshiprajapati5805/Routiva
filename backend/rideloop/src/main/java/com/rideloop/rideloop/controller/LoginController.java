
package com.rideloop.rideloop.controller;

import com.rideloop.rideloop.entity.Passenger;
import com.rideloop.rideloop.service.PassengerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/passengers")
@CrossOrigin(origins = "*")
public class LoginController {

    private final PassengerService passengerService;

    public LoginController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> data) {

        String email = data.get("email");
        String password = data.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email and password are required"));
        }

        Optional<Passenger> passenger =
                passengerService.loginPassenger(email, password);

        if (passenger.isPresent()) {

            Passenger savedPassenger = passenger.get();

            Map<String, Object> response = Map.of(
                    "id", savedPassenger.getId(),
                    "name", savedPassenger.getName(),
                    "email", savedPassenger.getEmail(),
                    "phone", savedPassenger.getPhone()
            );

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid email or password"));
    }
}
