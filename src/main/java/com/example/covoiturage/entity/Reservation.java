package com.example.covoiturage.entity;

import jakarta.persistence.*;


@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User passenger; // Passager (référence à l'entité User)

    @ManyToOne
    @JoinColumn(name = "ride_id", nullable = false)
    private Ride ride; // Trajet réservé (référence à l'entité Ride)

    @Column(nullable = false)
    private int seatsReserved; // Nombre de places réservées

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getPassenger() {
        return passenger;
    }

    public void setPassenger(User passenger) {
        this.passenger = passenger;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public int getSeatsReserved() {
        return seatsReserved;
    }

    public void setSeatsReserved(int seatsReserved) {
        this.seatsReserved = seatsReserved;
    }
}