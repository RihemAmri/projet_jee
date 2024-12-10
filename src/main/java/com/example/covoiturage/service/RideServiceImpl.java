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
    public Ride findRideById(Long rideId) {
        return rideRepository.findById(rideId).orElse(null); // Récupère le trajet par son ID ou renvoie null s'il n'existe pas
    }

}
