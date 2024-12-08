package com.example.covoiturage.repository;

import com.example.covoiturage.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {

    @Query("SELECT r FROM Ride r WHERE "
            + "(:departurePoint IS NULL OR LOWER(r.departurePoint) LIKE LOWER(CONCAT('%', :departurePoint, '%'))) AND "
            + "(:destination IS NULL OR LOWER(r.destination) LIKE LOWER(CONCAT('%', :destination, '%'))) AND "
            + "(:departureDate IS NULL OR r.departureDate = :departureDate) AND "
            + "(:maxPrice IS NULL OR r.pricePerSeat <= :maxPrice) AND "
            + "(:driverName IS NULL OR LOWER(CONCAT(r.driver.firstName, ' ', r.driver.lastName)) LIKE LOWER(CONCAT('%', :driverName, '%'))) ")
    List<Ride> searchRides(String departurePoint, String destination, Date departureDate, Double maxPrice, String driverName);
    List<Ride> findByDriverId(Long driverId);

}