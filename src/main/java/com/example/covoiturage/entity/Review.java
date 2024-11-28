package com.example.covoiturage.entity;


import jakarta.persistence.*;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User reviewer; // Utilisateur qui laisse l'évaluation

    @ManyToOne
    @JoinColumn(name = "ride_id", nullable = false)
    private Ride ride; // Trajet associé à l'évaluation

    @Column(nullable = false)
    private int rating; // Note de l'évaluation (par exemple, de 1 à 5)

    @Column
    private String comment; // Commentaire sur le trajet

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}