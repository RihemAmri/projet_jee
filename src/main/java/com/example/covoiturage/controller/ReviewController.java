package com.example.covoiturage.controller;

import com.example.covoiturage.entity.AppUser;
import com.example.covoiturage.entity.Review;
import com.example.covoiturage.entity.ReviewId;
import com.example.covoiturage.entity.Ride;
import com.example.covoiturage.service.ReviewService;
import com.example.covoiturage.service.RideService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private RideService rideService;

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

        // Créer l'objet Review
        Review review = new Review();
        review.setReviewId(reviewId);  // Associer la clé composée
        review.setRatingDriver(ratingDriver);  // Note du conducteur
        review.setCommentDriver(commentDriver);  // Commentaire du conducteur

        // Enregistrer l'évaluation via le service
        reviewService.submitReviewdriver(review);

        // Message de succès
        model.addAttribute("message", "Merci pour votre évaluation !");

        // Rediriger vers l'historique des réservations après soumission
        return "redirect:/reservationhistory";
    }



}
