package com.example.covoiturage.entity;
import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User driver; // Conducteur (référence à l'entité User)

    @Column(nullable = false)
    private String departurePoint; // Point de départ (ville ou adresse)

    @Column(nullable = false)
    private String destination; // Destination (ville ou adresse)

    @Column(nullable = false)
    private Date departureDate; // Date et heure du départ

    @Column(nullable = false)
    private int availableSeats; // Nombre de places disponibles

    @Column(nullable = false)
    private double pricePerSeat; // Prix par place

    @Column
    private String comments; // Commentaires facultatifs

    @Column
    private String restrictions; // Restrictions (par exemple, "non-fumeur")

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public String getDeparturePoint() {
        return departurePoint;
    }

    public void setDeparturePoint(String departurePoint) {
        this.departurePoint = departurePoint;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDepartureDate() {
        return departureDate;
    }
    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public double getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(double pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }
}