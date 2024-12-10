package com.example.covoiturage.service;

import com.example.covoiturage.entity.Ride;
import com.example.covoiturage.repository.RideRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;

    public RideServiceImpl(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    @Override
    public List<Ride> getAllRides() {
        return rideRepository.findAll(); // Retourner tous les trajets de la base de données
    }

    @Override
    public List<Ride> searchRides(String departurePoint, String destination, Date departureDate, Double maxPrice, String driverName) {
        // Si les critères sont vides, retourner tous les trajets
        if (departurePoint == null && destination == null && departureDate == null && maxPrice == null && driverName == null) {
            return getAllRides();
        }
        return rideRepository.searchRides(departurePoint, destination, departureDate, maxPrice, driverName);
    }
    @Override
    public Ride saveRide(Ride ride) {
        // Sauvegarde du Ride dans la base de données
        return rideRepository.save(ride);
    }
    @Override
    public Ride getRideById(Long id) {
        return rideRepository.findById(id).orElseThrow(() -> new RuntimeException("Ride not found"));
    }

    @Override
    public void updateRide(Ride ride) {
        rideRepository.save(ride);
    }

    @Override
    public void deleteRide(Long id) {
        rideRepository.deleteById(id);
    }

}
