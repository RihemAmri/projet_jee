package com.example.covoiturage.service;

import com.example.covoiturage.entity.AppUser;
import com.example.covoiturage.entity.Reservation;
import com.example.covoiturage.entity.Ride;
import com.example.covoiturage.repository.ReservationRepository;
import com.example.covoiturage.repository.RideRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import org.json.JSONObject;


@Service
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final ReservationRepository reservationRepository;
    private final EmailService emailService;
    private final GeocodingService geocodingService;
    // Ajouter votre service de géocodage


    public RideServiceImpl(RideRepository rideRepository, ReservationRepository reservationRepository, EmailService emailService, GeocodingService geocodingService) {
        this.rideRepository = rideRepository;
        this.reservationRepository = reservationRepository;
        this.emailService = emailService;
        this.geocodingService = geocodingService; // Injecter le service
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
        // Utiliser le service de géocodage pour obtenir les coordonnées
        try {
            double[] departureCoordinates = geocodingService.getCoordinates(ride.getDeparturePoint());
            double[] destinationCoordinates = geocodingService.getCoordinates(ride.getDestination());

            // Vérifier que les coordonnées de départ ne sont pas nulles
            if (departureCoordinates != null) {
                ride.setDepartureLatitude(departureCoordinates[0]); // Utilisation de [0] pour latitude
                ride.setDepartureLongitude(departureCoordinates[1]); // Utilisation de [1] pour longitude
            }

            // Vérifier que les coordonnées de destination ne sont pas nulles
            if (destinationCoordinates != null) {
                ride.setDestinationLatitude(destinationCoordinates[0]); // Utilisation de [0] pour latitude
                ride.setDestinationLongitude(destinationCoordinates[1]); // Utilisation de [1] pour longitude
            }
        } catch (Exception e) {
            // Gérer les erreurs de géocodage (par exemple, journaux ou actions spécifiques)
            System.err.println("Erreur lors du géocodage : " + e.getMessage());
        }

        // Sauvegarder le ride avec les coordonnées mises à jour
        return rideRepository.save(ride);
    }

    @Override
    public Ride getRideById(Long id) {
        return rideRepository.findById(id).orElseThrow(() -> new RuntimeException("Ride not found"));
    }



    @Override
    public void delete(Ride ride) {
        rideRepository.delete(ride);
    }
    @Override
    public Ride findRideById(Long id) {
        return rideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ride not found with id: " + id));
    }

    @Override
    public void updateRide(Ride ride) {
        rideRepository.save(ride); // Hibernate détecte automatiquement les modifications
    }





    public List<Ride> findRidesByDriver(AppUser driver) {
        return rideRepository.findByDriver(driver); // Méthode qui retourne les trajets d'un conducteur
    }
    @Override
    public void deleteRideAndNotify(Long rideId) {
        // Récupérer le trajet
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found with id: " + rideId));

        // Récupérer les réservations associées
        List<Reservation> reservations = ride.getReservations();

        // Notifier chaque passager et supprimer les réservations
        for (Reservation reservation : reservations) {
            reservationRepository.delete(reservation);

            String passengerEmail = reservation.getPassenger().getEmail();
            emailService.sendConfirmationEmail(
                    passengerEmail,
                    "Ride Cancelled Notification",
                    "Dear " + reservation.getPassenger().getFirstName() + ",\n\n"
                            + "We regret to inform you that the ride scheduled from "
                            + ride.getDeparturePoint() + " to " + ride.getDestination()
                            + " on " + ride.getDepartureDate() + " has been cancelled by the driver.\n\n"
                            + "We apologize for the inconvenience.\n\nBest regards,\nThe Covoiturage Team"
            );
        }

        // Notifier le conducteur
        String driverEmail = ride.getDriver().getEmail();
        emailService.sendConfirmationEmail(
                driverEmail,
                "Ride Deleted Successfully",
                "Dear " + ride.getDriver().getFirstName() + ",\n\n"
                        + "Your ride from " + ride.getDeparturePoint()
                        + " to " + ride.getDestination()
                        + " scheduled on " + ride.getDepartureDate() + " has been successfully deleted.\n\n"
                        + "Thank you for using our platform.\n\nBest regards,\nThe Covoiturage Team"
        );

        // Supprimer le trajet
        rideRepository.delete(ride);
    }
    @Override
    public void updateCoordinatesForAllRides() {
        List<Ride> rides = rideRepository.findAll();

        for (Ride ride : rides) {
            // Géocoder le point de départ
            double[] departureCoordinates = geocodingService.getCoordinates(ride.getDeparturePoint());
            if (departureCoordinates != null) {
                ride.setDepartureLatitude(departureCoordinates[0]);
                ride.setDepartureLongitude(departureCoordinates[1]);
            }

            // Géocoder le point de destination
            double[] destinationCoordinates = geocodingService.getCoordinates(ride.getDestination());
            if (destinationCoordinates != null) {
                ride.setDestinationLatitude(destinationCoordinates[0]);
                ride.setDestinationLongitude(destinationCoordinates[1]);
            }

            // Sauvegarder les modifications
            rideRepository.save(ride);
        }
    }

}
