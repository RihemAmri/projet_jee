package com.example.covoiturage.controller;

import com.example.covoiturage.entity.AppUser;
import com.example.covoiturage.entity.Ride;
import com.example.covoiturage.entity.Reservation;
import com.example.covoiturage.service.RideService;
import com.example.covoiturage.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Controller;

@Controller
public class ReservationController {

    @Autowired
    private RideService rideService; // Utilisation de l'interface RideService

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/book-ride/{rideId}")
    public String showBookingForm(@PathVariable Long rideId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Vérifier si l'utilisateur est connecté
//        /*if (userDetails == null) {
//            return "redirect:/loginn"; // Rediriger l'utilisateur vers la page de connexion s'il n'est pas connecté
//
//        }*/

        // Récupérer le trajet par son ID
        Ride ride = rideService.findRideById(rideId);

        // Passer les informations nécessaires au modèle
        model.addAttribute("ride", ride);
        model.addAttribute("rideId", rideId); // L'ID du trajet
        model.addAttribute("user", userDetails); // L'utilisateur connecté

        // Afficher le formulaire
        return "book-ride";
    }


    @PostMapping("/book-ride/{rideId}")
    public String bookRide(@PathVariable Long rideId, int numberOfSeats, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        // Vérifier si l'utilisateur est connecté
        /*if (userDetails == null) {
            return "redirect:/loginn"; // Si l'utilisateur n'est pas connecté, redirigez vers la page de connexion
        }*/

        // Convertir UserDetails en AppUser (s'il s'agit d'une instance d'AppUser)
        AppUser appUser = (AppUser) userDetails;

        // Récupérer le trajet
        Ride ride = rideService.findRideById(rideId);

        if (ride == null) {
            model.addAttribute("error", "Ride not found");
            return "rides"; // Retourner à la liste des trajets si la course n'existe pas
        }

        // Créer une nouvelle réservation
        Reservation reservation = new Reservation();
        reservation.setRide(ride);
        reservation.setSeatsReserved(numberOfSeats);
        reservation.setPassenger(appUser); // Utiliser AppUser ici

        // Calculer le prix total
        reservation.calculateTotalPrice();

        // Sauvegarder la réservation
        reservationService.saveReservation(reservation);

        // Passer les informations de réservation à la vue
        model.addAttribute("reservation", reservation);
        return "reservation-history"; // Vue de confirmation
    }

}

