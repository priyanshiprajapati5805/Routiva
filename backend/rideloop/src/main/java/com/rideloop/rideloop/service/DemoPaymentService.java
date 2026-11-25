
package com.rideloop.rideloop.service;

import org.springframework.stereotype.Service;

@Service
public class DemoPaymentService implements PaymentService {

    @Override
    public boolean processPayment(Long bookingId,
                                  double amount,
                                  String paymentMethod) {

        System.out.println("Processing Payment...");
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Amount: Rs. " + amount);
        System.out.println("Payment Method: " + paymentMethod);

        System.out.println("Demo Payment Successful!");

        return true;
    }

    @Override
    public boolean refundPayment(Long bookingId) {

        System.out.println("Refund processed for Booking ID: "
                + bookingId);

        return true;
    }

    @Override
    public String getPaymentStatus(Long bookingId) {

        return "SUCCESS";
    }
}
