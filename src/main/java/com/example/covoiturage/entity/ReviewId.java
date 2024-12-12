package com.example.covoiturage.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ReviewId implements Serializable {

    private Long rideId;       // Identifiant du trajet
    private Long passengerId;  // Identifiant du passager
    private Long driverId;     // Identifiant du conducteur

    // Getters et Setters
    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    // Méthodes equals et hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewId reviewId = (ReviewId) o;
        return Objects.equals(rideId, reviewId.rideId) &&
                Objects.equals(passengerId, reviewId.passengerId) &&
                Objects.equals(driverId, reviewId.driverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rideId, passengerId, driverId);
    }
}
