
package com.rideloop.rideloop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "drivers")
public class Driver extends User {

    private String drivingLicense;
    private String vehicleNumber;
    private String vehicleModel;
    private int totalSeats;

    // Driver-wise price adjustment
    private double priceAdjustment = 0.0;

    public Driver() {
        super();
    }

    public Driver(String name, String email, String phone,
                  String password, String drivingLicense,
                  String vehicleNumber, String vehicleModel,
                  int totalSeats) {

        super(name, email, phone, password);
        this.drivingLicense = drivingLicense;
        this.vehicleNumber = vehicleNumber;
        this.vehicleModel = vehicleModel;
        this.totalSeats = totalSeats;
        this.priceAdjustment = 0.0;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public double getPriceAdjustment() {
        return priceAdjustment;
    }

    public void setPriceAdjustment(double priceAdjustment) {
        this.priceAdjustment = priceAdjustment;
    }
}
