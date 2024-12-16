package com.example.covoiturage.controller;

import com.example.covoiturage.entity.Review;
import com.example.covoiturage.repository.ReviewRepository;
import com.example.covoiturage.service.ReviewService;
import com.example.covoiturage.service.UserService;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.covoiturage.entity.AppUser;
import com.example.covoiturage.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository; // Pour récupérer les utilisateurs de la DB

    @Autowired
    private  UserService userService;
    @Autowired
    private ReviewService reviewService;


    // Page d'accueil (about)
    @GetMapping({"", "/"})
    public String home(Model model,HttpSession session) {
        String role = (String) session.getAttribute("role");  // Récupère le rôle depuis la session
        model.addAttribute("role", role);  // Passe le rôle à la vue
        return "about";
    }

    // Page de connexion (loginn)
    @GetMapping("/loginn")
    public String loginn() {
        return "loginn"; // Affiche la page de connexion
    }

    // Gérer la soumission du formulaire de connexion (POST)
    @PostMapping("/loginn")
    public String handleLogin(String email, String password, HttpSession session,Model model) {
        // Vérifier si l'utilisateur existe dans la base de données

        AppUser user = userRepository.findByEmail(email);

        if (user != null ) {
            System.out.println("linaaaaaaaaaaa");
            // Si les informations de connexion sont valides, créer une session
            session.setAttribute("email", user.getEmail());
            session.setAttribute("id", user.getId());
            session.setAttribute("role", user.getRole());
            model.addAttribute("role", user.getRole());
            System.out.println(user.getRole());

            if ("driver".equals(user.getRole())) {
                return "redirect:/Myrides";  // Page pour le conducteur
            } else if ("passenger".equals(user.getRole())) {
                return "redirect:/rides";  // Page pour le passager
            }
            return "loginn";
        } else {
            // Si les informations de connexion sont invalides, redirection vers la page de login
            return "redirect:/loginn";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalider la session à la déconnexion
        return "redirect:/";
    }
    @GetMapping("/profilePass/{id}")
    public String getUserProfile(@PathVariable Long id, Model model) {
        // Récupère les données de l'utilisateur (ou du passager)
        AppUser user = userService .findById(id);

        List<Review> reviews = reviewService.findReviewsByUserId(user.getId());

        List<Double> passengerRatings = new ArrayList<>();
        for (Review review : reviews) {
            passengerRatings.add(review.getRatingPassenger());
        }
        List<Double> ratingDriver = new ArrayList<>();
        for (Review review : reviews) {
            ratingDriver.add(review.getRatingDriver());
        }
        System.out.println("lina");
        model.addAttribute("user", user);
        model.addAttribute("reviews", reviews);
        System.out.println(passengerRatings);
        model.addAttribute("passengerRatings", passengerRatings);

        model.addAttribute("ratingDriver", ratingDriver);
        System.out.println(ratingDriver);
        System.out.println(user.getRole());


            return "profilePass"; // Charge la page profile.html
    }
    @GetMapping("/profileDriver/{id}")
    public String getUserProfileDriver(@PathVariable Long id, Model model) {
        // Récupère les données de l'utilisateur (ou du passager)
        AppUser user = userService .findById(id);

        List<Review> reviews = reviewService.findReviewsBydriver(user.getId());
        System.out.println(user.getId());
        System.out.println(reviews);
        List<Double> passengerRatings = new ArrayList<>();
        for (Review review : reviews) {
            passengerRatings.add(review.getRatingPassenger());
        }
        List<Double> ratingDriver = new ArrayList<>();
        for (Review review : reviews) {
            ratingDriver.add(review.getRatingDriver());
        }
        System.out.println("lina");
        model.addAttribute("user", user);
        model.addAttribute("reviews", reviews);
        System.out.println(passengerRatings);
        model.addAttribute("passengerRatings", passengerRatings);

        model.addAttribute("ratingDriver", ratingDriver);
        System.out.println(ratingDriver);
        System.out.println(user.getRole());


        return "profileDriver"; // Charge la page profile.html
    }

    @GetMapping("/editprofile/{id}")
    public String showUpdateProfilePage(@PathVariable("id") Long userId, Model model) {
        // Charger les données de l'utilisateur par son ID
        AppUser user = userService.findById(userId);

        // Ajouter l'utilisateur comme attribut du modèle
        model.addAttribute("user", user);

        // Retourner le nom de la vue
        return "editprofile"; // Nom du fichier HTML à afficher
    }


    @PostMapping("/editprofile/{id}")
    public String updateUser(@ModelAttribute AppUser user) {
        userService.updateUser(user); // Appelle le service pour sauvegarder les modifications
        if("driver".equals(user.getRole())){
            return "redirect:/profileDriver/{id}";
        }
        return "redirect:/profilePass/" + user.getId(); // Redirige vers la page de profil
    }




}
