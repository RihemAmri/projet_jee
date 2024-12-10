package com.example.covoiturage.service;

import com.example.covoiturage.entity.Ride;

import java.util.Date;
import java.util.List;

public interface RideService {
    Ride saveRide(Ride ride);
    List<Ride> getAllRides();
    // Méthode pour rechercher les trajets en fonction des critères
    List<Ride> searchRides(String departurePoint, String destination, Date departureDate, Double maxPrice, String driverName);




    Ride findRideById(Long rideId);
}