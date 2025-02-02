package com.example.covoiturage.entity;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id",nullable = false)
    private AppUser driver; // Conducteur (référence à l'entité User)

    @Column(nullable = false)
    private String departurePoint; // Point de départ (ville ou adresse)

    @Column(nullable = false)
    private String destination; // Destination (ville ou adresse)
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date departureDate; // Utilisation de l'annotation @DateTimeFormat

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

    public AppUser getDriver() {
        return driver;
    }

    public void setDriver(AppUser driver) {
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

    @OneToMany(mappedBy = "ride")
    private List<Reservation> reservations;

    public List<Reservation> getReservations() {
        return reservations;
    }
    @Column(nullable = false)
    private double departureLatitude;

    @Column(nullable = false)
    private double departureLongitude;

    @Column(nullable = false)
    private double destinationLatitude;

    @Column(nullable = false)
    private double destinationLongitude;

    // Getters and Setters
    public double getDepartureLatitude() {
        return departureLatitude;
    }

    public void setDepartureLatitude(double departureLatitude) {
        this.departureLatitude = departureLatitude;
    }

    public double getDepartureLongitude() {
        return departureLongitude;
    }

    public void setDepartureLongitude(double departureLongitude) {
        this.departureLongitude = departureLongitude;
    }

    public double getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(double destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public double getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(double destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

}