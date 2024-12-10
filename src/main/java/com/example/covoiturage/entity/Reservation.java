package com.example.covoiturage.entity;

import jakarta.persistence.*;


@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser passenger;
    @ManyToOne
    @JoinColumn(name = "ride_id", nullable = false)
    private Ride ride; // Trajet réservé (référence à l'entité Ride)

    @Column(nullable = false)
    private int seatsReserved; // Nombre de places réservées

    @Column(nullable = false)
    private double totalPrice; // Le prix total calculé automatiquement

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getPassenger() {
        return passenger;
    }

    public void setPassenger(AppUser passenger) {
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

    public void calculateTotalPrice() {
        this.totalPrice = this.seatsReserved * this.ride.getPricePerSeat();
    }
    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}