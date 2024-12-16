package com.example.covoiturage.service;

import com.example.covoiturage.entity.Review;

import java.util.List;

public interface ReviewService {
    List<Review> getReviewsByRideId(Long idRide);
    void submitReviewdriver(Review review);

    List<Review> findReviewsByUserId(Long id);
    List<Review> findReviewsBydriver(Long driverId);
}
