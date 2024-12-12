package com.example.covoiturage.service;

import com.example.covoiturage.entity.Review;
import com.example.covoiturage.repository.ReviewRepository;
import com.example.covoiturage.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
