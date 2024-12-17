package com.example.covoiturage.controller;

import com.example.covoiturage.entity.*;
import com.example.covoiturage.repository.ReviewRepository;
import com.example.covoiturage.repository.UserRepository;
import com.example.covoiturage.service.ReservationService;
import com.example.covoiturage.service.ReviewService;
import com.example.covoiturage.service.RideService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private RideService rideService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository  userRepository;

    // Méthode pour afficher le formulaire de notation (avec l'ID du trajet)
    @GetMapping("/ratedriver/{rideId}")
    public String showRatingForm(@PathVariable("rideId") Long rideId, Model model) {
        model.addAttribute("rideId", rideId); // Ajout de l'ID du trajet à la vue
        model.addAttribute("review", new Review()); // Créer une nouvelle évaluation vide
        return "driverReview"; // Renvoie le nom de la vue (page HTML)
    }

    // Méthode pour traiter la soumission du formulaire de notation
    @PostMapping("/ratedriver")
    public String submitRating(
            @RequestParam("rideId") Long rideId,  // Récupérer l'ID du trajet envoyé dans le formulaire
            @RequestParam("ratingDriver") Double ratingDriver, // Récupérer la note du conducteur
            @RequestParam("commentDriver") String commentDriver, // Récupérer le commentaire du conducteur
            HttpServletRequest request,
            Model model) {

        // Vérifier que l'ID du trajet existe
        System.out.println("linaaaa"+rideId );
        Ride ride = rideService.getRideById(rideId);
        System.out.println("3aslemaaa");
        // Récupérer le trajet via son ID
        if (ride == null) {

            System.out.println("ride is null");
            model.addAttribute("message", "Trajet introuvable.");
            return "errorPage"; // Rediriger en cas de problème
        }

        // Récupérer l'ID du conducteur à partir du trajet
        AppUser driverId = ride.getDriver();

        // Récupérer l'ID du passager depuis la session
        Long passengerId = (Long) request.getSession().getAttribute("id");

        // Vérifier que l'ID du passager existe dans la session
        if (passengerId == null) {
            model.addAttribute("message", "Aucun passager trouvé dans la session.");
            return "loginn"; // Rediriger si le passager n'est pas trouvé
        }

        // Créer la clé composée pour la revue
        ReviewId reviewId = new ReviewId();
        reviewId.setRideId(rideId);
        reviewId.setDriverId(driverId.getId());
        reviewId.setPassengerId(passengerId);


        Optional<Review> existingReview = reviewRepository.findById(reviewId);

        if (existingReview.isPresent()) {
            Review review = existingReview.get();
            review.setRatingDriver(ratingDriver);
            review.setCommentDriver(commentDriver);
            reviewRepository.save(review);
        } else {
            Review review = new Review();
            review.setReviewId(reviewId);
            review.setRatingDriver(ratingDriver);
            review.setCommentDriver(commentDriver);
            reviewRepository.save(review);
        }// Commentaire du conducteur





        // Enregistrer l'évaluation via le service
        //reviewService.submitReviewdriver(review);

        // Message de succès
        model.addAttribute("message", "Merci pour votre évaluation !");

        // Rediriger vers l'historique des réservations après soumission
        return "redirect:/reservationhistory";
    }



    @GetMapping("/ratepassenger/{reservationId}")
    public String showRatingFormPassenger(@PathVariable("reservationId") Long reservationId, Model model) {
        model.addAttribute("reservationId", reservationId); // Ajout de l'ID du trajet à la vue
        model.addAttribute("review", new Review()); // Créer une nouvelle évaluation vide
        return "passengerReview"; // Renvoie le nom de la vue (page HTML)
    }

    // Méthode pour traiter la soumission du formulaire de notation
    @PostMapping("/ratepassenger")
    public String submitRatingPassenger(
            @RequestParam("reservationId") Long reservationId,  // Récupérer l'ID du trajet envoyé dans le formulaire
            @RequestParam("ratingPassenger") Double ratingPassenger, // Récupérer la note du conducteur
            @RequestParam("commentPassenger") String commentPassenger, // Récupérer le commentaire du conducteur
            HttpServletRequest request,
            Model model) {

        // Vérifier que l'ID du trajet existe


        Reservation resr = reservationService.findById(reservationId);

        // Récupérer le trajet via son ID
        if (resr == null) {
            model.addAttribute("message", "Trajet introuvable.");
            return "errorPage"; // Rediriger en cas de problème
        }

        // Récupérer l'ID du conducteur à partir du trajet
        Long passengerId = resr.getPassenger().getId();
        long rideId=resr.getRide().getId();

        // Récupérer l'ID du passager depuis la session
        Long driverId = (Long) request.getSession().getAttribute("id");

        // Vérifier que l'ID du passager existe dans la session
        if (passengerId == null) {
            model.addAttribute("message", "Aucun passager trouvé dans la session.");
            return "loginn"; // Rediriger si le passager n'est pas trouvé
        }

        // Créer la clé composée pour la revue
        ReviewId reviewId = new ReviewId();
        reviewId.setRideId(rideId);
        reviewId.setPassengerId(passengerId);
        reviewId.setDriverId(driverId);

        Optional<Review> existingReview = reviewRepository.findById(reviewId);

        if (existingReview.isPresent()) {
            Review review = existingReview.get();
            review.setRatingPassenger(ratingPassenger);
            review.setCommentPassenger(commentPassenger);
            reviewRepository.save(review);
        } else {
            Review review = new Review();
            review.setReviewId(reviewId);
            review.setRatingPassenger(ratingPassenger);
            review.setCommentPassenger(commentPassenger);
            reviewRepository.save(review);
        }// Commentaire du conducteur

        // Enregistrer l'évaluation via le service


        // Message de succès
        model.addAttribute("message", "Merci pour votre évaluation !");

        // Rediriger vers l'historique des réservations après soumission
        return "driver-reservation-history";
    }
    @GetMapping("/Review")
    public String getReviews(Model model, HttpSession session) {
        System.out.println("linaaaa fi review");

        // Récupérer les informations de session
        Long sessionId = (Long) session.getAttribute("id");
        System.out.println(sessionId);
        String role = (String) session.getAttribute("role");

        // Récupérer les reviews en fonction du rôle
        List<Review> reviews = reviewService.getReviewsByRole(role, sessionId);

        // Préparer les données simplifiées pour la vue
        List<Map<String, Object>> reviewDetails = reviews.stream().map(review -> {
            Map<String, Object> details = new HashMap<>();

            // Ajouter les informations du review
            if ("driver".equals(role)) {
                details.put("name", userRepository.findById(review.getPassenger().getId())
                        .map(AppUser::getFirstName)
                        .orElse("Unknown"));
                details.put("rating", review.getRatingDriver());
                details.put("comment", review.getCommentDriver());
            } else {
                details.put("name", userRepository.findById(review.getDriver().getId())
                        .map(AppUser::getFirstName)
                        .orElse("Unknown"));
                details.put("rating", review.getRatingPassenger());
                details.put("comment", review.getCommentPassenger());
            }

            // Récupérer et ajouter les informations de la ride
            details.put("rideId", review.getRide().getId());
            details.put("departurePoint", review.getRide().getDeparturePoint());
            details.put("destination", review.getRide().getDestination());
            details.put("departureDate", review.getRide().getDepartureDate());

            return details;
        }).toList();

        // Ajout des informations au modèle
        model.addAttribute("role", role);
        model.addAttribute("reviewDetails", reviewDetails);

        return "Review";
    }


}
