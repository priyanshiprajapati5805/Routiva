
package com.rideloop.rideloop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "passengers")
public class Passenger extends User {

    private String preferredGender;
    private String emergencyContact;

    public Passenger() {
        super();
    }

    public Passenger(String name, String email, String phone,
                     String password, String preferredGender,
                     String emergencyContact) {

        super(name, email, phone, password);
        this.preferredGender = preferredGender;
        this.emergencyContact = emergencyContact;
    }

    public String getPreferredGender() {
        return preferredGender;
    }

    public void setPreferredGender(String preferredGender) {
        this.preferredGender = preferredGender;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }
}
