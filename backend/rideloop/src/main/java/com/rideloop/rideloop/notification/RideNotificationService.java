
package com.rideloop.rideloop.notification;

import org.springframework.stereotype.Service;

@Service
public class RideNotificationService {

    public void sendNotification(Long rideId,
                                 String passengerName,
                                 String driverName) {

        RideNotificationTask task =
                new RideNotificationTask(
                        rideId,
                        passengerName,
                        driverName
                );

        Thread thread = new Thread(task);

        thread.start();
    }
}

