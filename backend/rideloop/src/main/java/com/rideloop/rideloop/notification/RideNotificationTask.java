
package com.rideloop.rideloop.notification;

public class RideNotificationTask implements Runnable {

    private Long rideId;
    private String passengerName;
    private String driverName;

    public RideNotificationTask(Long rideId,
                                String passengerName,
                                String driverName) {

        this.rideId = rideId;
        this.passengerName = passengerName;
        this.driverName = driverName;
    }

    @Override
    public void run() {

        System.out.println("Processing Ride Notification...");

        System.out.println("Ride ID: " + rideId);
        System.out.println("Passenger: " + passengerName);
        System.out.println("Driver: " + driverName);

        System.out.println(
            "Notification sent to driver successfully!"
        );
    }
}
