
package com.rideloop.rideloop.service;

public interface PaymentService {

    boolean processPayment(
        Long bookingId,
        double amount,
        String paymentMethod
    );

    boolean refundPayment(Long bookingId);

    String getPaymentStatus(Long bookingId);
}
