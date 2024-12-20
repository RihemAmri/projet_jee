package com.example.covoiturage.controller;

import com.example.covoiturage.entity.AppUser;
import com.example.covoiturage.entity.Ride;
import com.example.covoiturage.entity.Reservation;
import com.example.covoiturage.service.EmailService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @Autowired
    private EmailService emailService; // Inject the EmailService


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
    public String bookRide(@PathVariable Long rideId, int numberOfSeats, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        try {
            Long userId = (Long) session.getAttribute("id");
            System.out.println("User ID in session: " + userId); // Vérifiez si l'ID est null ou valide

            if (userId == null) {
                System.out.println("User is not logged in. Redirecting to login.");
                return "redirect:/loginn";
            }
            System.out.println(userId);

            // Retrieve the ride
            Ride ride = rideService.findRideById(rideId);

            if (ride == null) {
                model.addAttribute("error", "Ride not found");
                return "rides"; // Return to the list of rides if the ride does not exist
            }

            // Check if there are enough available seats
            if (ride.getAvailableSeats() < numberOfSeats) {
                model.addAttribute("error", "Not enough available seats.");
                return "rides"; // Return to the list of rides if there aren't enough seats
            }

            // Retrieve the user by their ID from the session
            AppUser appUser = userService.findById(userId);

            if (appUser == null) {
                model.addAttribute("error", "User not found");
                return "rides"; // Return to the list of rides if the user is not found
            }

            // Create a new reservation
            Reservation reservation = new Reservation();
            reservation.setRide(ride);
            reservation.setSeatsReserved(numberOfSeats);
            reservation.setPassenger(appUser); // Associate the user with the reservation

            // Calculate the total price
            reservation.calculateTotalPrice();

            // Save the reservation
            reservationService.saveReservation(reservation);

            // Update the available seats for the ride
            ride.setAvailableSeats(ride.getAvailableSeats() - numberOfSeats);

            // Save the updated ride
            rideService.saveRide(ride);

            // Send email notifications

            // 1. Send a confirmation email to the passenger
            String passengerEmail = appUser.getEmail();
            String passengerSubject = "Reservation Confirmed";
            String passengerText = "Your reservation for the ride \"" + ride.getDeparturePoint() + " - " + ride.getDestination() + "\" has been confirmed By the Driver: "+ride.getDriver().getLastName()+" "+ride.getDriver().getFirstName()+" Number of seats reserved: " + numberOfSeats + ".";

            emailService.sendConfirmationEmail(passengerEmail, passengerSubject, passengerText);

            // 2. Send an email to the driver
            String driverEmail = ride.getDriver().getEmail(); // Ensure you have the correct reference to the driver in the Ride object
            String driverSubject = "New Reservation";
            String driverText = "You have a new reservation for the ride \"" + ride.getDeparturePoint() + " - " + ride.getDestination() + "\". Passenger: " + appUser.getFirstName() + " " + appUser.getLastName() + ". Number of seats reserved: " + numberOfSeats + ".";
            emailService.sendConfirmationEmail(driverEmail, driverSubject, driverText);

            // Pass the reservation information to the view
            //model.addAttribute("reservation", reservation);
            redirectAttributes.addFlashAttribute("success", "Your reservation has been successfully completed!");
            return "redirect:/reservationhistory"; // Confirmation view
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while booking the ride.");
            return "rides"; // Return to the list of rides in case of an error
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
    public String cancelReservation(@PathVariable Long reservationId, HttpSession session, Model model) {
        try {
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

            // Envoyer un email au conducteur
            String driverEmail = ride.getDriver().getEmail(); // S'assurer que le conducteur est bien associé à la course
            String driverSubject = "Reservation Canceled";
            String driverText = "The passenger " + reservation.getPassenger().getFirstName() + " " + reservation.getPassenger().getLastName()
                    + " has canceled their reservation for the ride \"" + ride.getDeparturePoint() + " - " + ride.getDestination()
                    + "\". Number of seats canceled: " + reservation.getSeatsReserved() + ".";

            emailService.sendConfirmationEmail(driverEmail, driverSubject, driverText);

            // Envoyer un email au passager
            String passengerEmail = reservation.getPassenger().getEmail();
            String passengerSubject = "Reservation Canceled Successfully";
            String passengerText = "Your reservation for the ride \"" + ride.getDeparturePoint() + " - " + ride.getDestination()
                    + "\" has been successfully canceled. Number of seats canceled: " + reservation.getSeatsReserved() + ".";

            emailService.sendConfirmationEmail(passengerEmail, passengerSubject, passengerText);

            return "redirect:/reservationhistory?success=Reservation canceled";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while canceling the reservation.");
            return "error"; // Afficher une page d'erreur en cas de problème
        }
    }

    @GetMapping("/driver/reservationhistory")
    public String viewDriverReservationHistory(HttpSession session, Model model) {
        try {
            // Vérifier si l'utilisateur est connecté
            Long userId = (Long) session.getAttribute("id");
            if (userId == null) {
                return "redirect:/loginn"; // Rediriger vers la page de connexion si l'utilisateur n'est pas connecté
            }

            // Récupérer l'utilisateur (le conducteur)
            AppUser appUser = userService.findById(userId);
            if (appUser == null) {
                model.addAttribute("error", "User not found");
                return "error"; // Afficher une page d'erreur si l'utilisateur n'existe pas
            }

            // Récupérer tous les trajets que l'utilisateur (le conducteur) a publiés
            List<Ride> rides = rideService.findRidesByDriver(appUser);

            // Diviser les réservations en passées et à venir pour chaque trajet
            List<Reservation> pastReservations = rides.stream()
                    .flatMap(ride -> ride.getReservations().stream()) // Récupérer les réservations liées à chaque trajet
                    .filter(reservation -> reservation.getRide().getDepartureDate().before(new Date()))
                    .collect(Collectors.toList());

            List<Reservation> upcomingReservations = rides.stream()
                    .flatMap(ride -> ride.getReservations().stream()) // Récupérer les réservations liées à chaque trajet
                    .filter(reservation -> reservation.getRide().getDepartureDate().after(new Date()))
                    .collect(Collectors.toList());

            // Ajouter les réservations au modèle
            model.addAttribute("pastReservations", pastReservations);
            model.addAttribute("upcomingReservations", upcomingReservations);

            return "driver-reservation-history"; // Vue pour l'historique des réservations du conducteur
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while fetching reservation history.");
            return "error"; // Afficher une page d'erreur en cas de problème
        }
    }
    /*@GetMapping("/test-email")
    public String testEmail() {
        try {
            emailService.sendConfirmationEmail("nadabentaher2003@gmail.com", "Test Email", "This is a test email.");
            return "Email sent successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send email. Error: " + e.getMessage();
        }
    }*/




}

