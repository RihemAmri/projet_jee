package com.example.covoiturage.repository;

import com.example.covoiturage.entity.Review;
import com.example.covoiturage.entity.ReviewId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.reviewId = :reviewId")
    Optional<Review> findById(@Param("reviewId") ReviewId reviewId);
    List<Review> findByReviewId_PassengerId(Long passengerId);


    // Recherche de Reviews par rideId à partir de la clé composite
    List<Review> findByReviewId_RideId(Long rideId);

    // Recherche de Reviews par driverId à partir de la clé composite
    List<Review> findByReviewId_DriverId(Long driverId);
}
