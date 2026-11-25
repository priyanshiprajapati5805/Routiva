
package com.rideloop.rideloop.controller;

import com.rideloop.rideloop.service.PaymentService;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Process Payment
    @PostMapping("/{bookingId}")
    public String processPayment(
            @PathVariable Long bookingId,
            @RequestParam double amount,
            @RequestParam String paymentMethod) {

        boolean success = paymentService.processPayment(
                bookingId,
                amount,
                paymentMethod
        );

        return success
                ? "Payment Successful!"
                : "Payment Failed!";
    }

    // Refund Payment
    @PostMapping("/refund/{bookingId}")
    public String refundPayment(@PathVariable Long bookingId) {

        boolean success =
                paymentService.refundPayment(bookingId);

        return success
                ? "Refund Successful!"
                : "Refund Failed!";
    }

    // Payment Status
    @GetMapping("/{bookingId}")
    public Map<String, String> getPaymentStatus(
            @PathVariable Long bookingId) {

        String status =
                paymentService.getPaymentStatus(bookingId);

        return Map.of("bookingId", bookingId.toString(),
                      "paymentStatus", status);
    }
}
