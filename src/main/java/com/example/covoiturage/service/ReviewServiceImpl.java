package com.example.covoiturage.service;

import com.example.covoiturage.entity.Review;
import com.example.covoiturage.repository.ReviewRepository;
import com.example.covoiturage.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public List<Review> getReviewsByRideId(Long idRide) {
        return List.of();
    }

    @Override
    public void submitReviewdriver(Review review) {
        // Vérification ou logique métier si nécessaire
        reviewRepository.save(review);
    }
    @Override
    public List<Review> findReviewsByUserId(Long passengerId) {
        // Utiliser l'ID du passager pour récupérer ses évaluations
        return reviewRepository.findByReviewId_PassengerId(passengerId);
    }
    @Override
    public List<Review> findReviewsBydriver(Long driverId) {
        // Utiliser l'ID du passager pour récupérer ses évaluations
        return reviewRepository.findByReviewId_DriverId(driverId);
    }
    @Override
    public List<Review> getReviewsByRole(String role, Long sessionId) {
        if (role.equals("driver")) {
            return reviewRepository.findByReviewId_DriverId(sessionId);
        } else if (role.equals("passenger")) {
            return reviewRepository.findByReviewId_PassengerId(sessionId);
        }
        return new ArrayList<>();
    }




}
