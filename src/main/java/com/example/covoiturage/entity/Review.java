package com.example.covoiturage.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class Review {

    @EmbeddedId
    private ReviewId reviewId; // Clé composée de la classe ReviewId

    @ManyToOne
    @JoinColumn(name = "rideId", referencedColumnName = "id", insertable = false, updatable = false)
    private Ride ride; // Lien avec l'entité Ride

    @ManyToOne
    @JoinColumn(name = "passengerId", referencedColumnName = "id", insertable = false, updatable = false)
    private AppUser passenger; // Lien avec l'entité AppUser (passager)

    @ManyToOne
    @JoinColumn(name = "driverId", referencedColumnName = "id", insertable = false, updatable = false)
    private AppUser driver; // Lien avec l'entité AppUser (conducteur)

    private Double ratingDriver; // Note du conducteur
    private String commentDriver;
    private Double ratingPassenger; // Note du conducteur
    private String commentPassenger;// Commentaire du conducteur

    // Getters et Setters
    public ReviewId getReviewId() {
        return reviewId;
    }

    public void setReviewId(ReviewId reviewId) {
        this.reviewId = reviewId;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public AppUser getPassenger() {
        return passenger;
    }

    public void setPassenger(AppUser passenger) {
        this.passenger = passenger;
    }

    public AppUser getDriver() {
        return driver;
    }

    public void setDriver(AppUser driver) {
        this.driver = driver;
    }

    public Double getRatingDriver() {
        return ratingDriver;
    }

    public void setRatingDriver(Double ratingDriver) {
        this.ratingDriver = ratingDriver;
    }

    public String getCommentDriver() {
        return commentDriver;
    }

    public void setCommentDriver(String commentDriver) {
        this.commentDriver = commentDriver;
    }

    public Double getRatingPassenger() {
        return ratingPassenger;
    }

    public void setRatingPassenger(Double ratingPassenger) {
        this.ratingPassenger = ratingPassenger;
    }

    public String getCommentPassenger() {
        return commentPassenger;
    }

    public void setCommentPassenger(String commentPassenger) {
        this.commentPassenger = commentPassenger;
    }
}
