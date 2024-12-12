package com.example.covoiturage.controller;

import com.example.covoiturage.entity.AppUser;
import com.example.covoiturage.entity.Ride;
import com.example.covoiturage.entity.Reservation;
import com.example.covoiturage.service.RideService;
import com.example.covoiturage.service.ReservationService;
import com.example.covoiturage.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ReservationController {

    @Autowired
    private RideService rideService; // Utilisation de l'interface RideService

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserServiceImpl userService;

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
    public String bookRide(@PathVariable Long rideId, int numberOfSeats, HttpSession session, Model model) {
        try {
            // Vérifier si l'utilisateur est connecté en récupérant l'ID de l'utilisateur dans la session
            Long userId = (Long) session.getAttribute("id");

            if (userId == null) {
                // Si l'utilisateur n'est pas connecté, rediriger vers la page de connexion
                return "redirect:/loginn";
            }
            System.out.println(userId);

            // Récupérer le trajet
            Ride ride = rideService.findRideById(rideId);

            if (ride == null) {
                model.addAttribute("error", "Ride not found");
                return "rides"; // Retourner à la liste des trajets si la course n'existe pas
            }

            // Vérifier s'il y a suffisamment de places disponibles
            if (ride.getAvailableSeats() < numberOfSeats) {
                model.addAttribute("error", "Not enough available seats.");
                return "rides"; // Retourner à la liste des trajets si pas assez de places
            }

            // Récupérer l'utilisateur avec l'ID récupéré de la session
            AppUser appUser = userService.findById(userId); // Trouver l'utilisateur par son ID

            if (appUser == null) {
                model.addAttribute("error", "User not found");
                return "rides"; // Retourner à la liste des trajets si l'utilisateur n'est pas trouvé
            }

            // Créer une nouvelle réservation
            Reservation reservation = new Reservation();
            reservation.setRide(ride);
            reservation.setSeatsReserved(numberOfSeats);
            reservation.setPassenger(appUser); // Associer l'utilisateur à la réservation

            // Calculer le prix total
            reservation.calculateTotalPrice();

            // Sauvegarder la réservation
            reservationService.saveReservation(reservation);

            // Mettre à jour le nombre de places disponibles dans la course
            ride.setAvailableSeats(ride.getAvailableSeats() - numberOfSeats);

            // Sauvegarder la course mise à jour
            rideService.saveRide(ride);

            // Passer les informations de réservation à la vue
            model.addAttribute("reservation", reservation);
            return "reservationhistory"; // Vue de confirmation
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while booking the ride.");
            return "rides"; // Retourner à la liste des trajets en cas d'erreur
        }
    }
    @GetMapping("/reservationhistory")
    public String viewReservationHistory(HttpSession session, Model model) {
        try {
            // Vérifier si l'utilisateur est connecté en récupérant l'ID de l'utilisateur dans la session
            Long userId = (Long) session.getAttribute("id");
            if (userId == null) {
                return "redirect:/loginn"; // Rediriger vers la page de connexion si l'utilisateur n'est pas connecté
            }

            // Récupérer l'utilisateur
            AppUser appUser = userService.findById(userId);
            if (appUser == null) {
                model.addAttribute("error", "User not found");
                return "error"; // Afficher une page d'erreur si l'utilisateur n'existe pas
            }

            // Récupérer toutes les réservations de cet utilisateur
            List<Reservation> reservations = appUser.getReservations(); // Assurez-vous que la relation est bien mappée dans AppUser

            // Diviser les réservations en passées et à venir
            List<Reservation> pastReservations = reservations.stream()
                    .filter(reservation -> reservation.getRide().getDepartureDate().before(new Date()))
                    .collect(Collectors.toList());

            List<Reservation> upcomingReservations = reservations.stream()
                    .filter(reservation -> reservation.getRide().getDepartureDate().after(new Date()))
                    .collect(Collectors.toList());

            // Ajouter les réservations au modèle
            model.addAttribute("pastReservations", pastReservations);
            model.addAttribute("upcomingReservations", upcomingReservations);

            return "reservationhistory"; // Retourner la vue correspondante
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while fetching reservation history.");
            return "error"; // Afficher une page d'erreur en cas de problème
        }
    }
    @PostMapping("/cancel-reservation/{reservationId}")
    public String cancelReservation(@PathVariable Long reservationId, HttpSession session) {
        // Vérifier si l'utilisateur est connecté
        Long userId = (Long) session.getAttribute("id");
        if (userId == null) {
            return "redirect:/loginn";
        }

        // Récupérer la réservation
        Reservation reservation = reservationService.findById(reservationId);

        if (reservation == null || !reservation.getPassenger().getId().equals(userId)) {
            return "redirect:/reservationhistory?error=Unauthorized"; // Vérification de l'autorisation
        }

        // Récupérer la course associée et mettre à jour les places disponibles
        Ride ride = reservation.getRide();
        ride.setAvailableSeats(ride.getAvailableSeats() + reservation.getSeatsReserved());
        rideService.saveRide(ride);

        // Supprimer la réservation
        reservationService.deleteReservation(reservationId);

        return "redirect:/reservationhistory?success=Reservation cancelled";
    }




}

