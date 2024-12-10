package com.example.covoiturage.controller;

import com.example.covoiturage.service.UserService;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.covoiturage.entity.AppUser;
import com.example.covoiturage.repository.UserRepository;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository; // Pour récupérer les utilisateurs de la DB




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
    public String handleLogin(String email, String password, HttpSession session, Model model) {
        // Récupérer l'utilisateur par email
        AppUser user = userRepository.findByEmail(email);

        // Vérifier si l'utilisateur existe et si le mot de passe est valide
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // Créer une session manuelle
            session.setAttribute("email", user.getEmail());
            session.setAttribute("id", user.getId());
            session.setAttribute("role", user.getRole());
            model.addAttribute("role", user.getRole());

            // Rediriger l'utilisateur en fonction de son rôle
            if ("driver".equals(user.getRole())) {
                return "redirect:/Myrides";  // Page pour le conducteur
            } else if ("passenger".equals(user.getRole())) {
                return "redirect:/rides";  // Page pour le passager
            } else {
                // Par défaut, rediriger vers la page d'accueil ou autre page
                return "redirect:/";
            }
        } else {
            // Si l'utilisateur n'est pas trouvé ou le mot de passe est incorrect, rediriger vers la page de login
            return "redirect:/loginn";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalider la session à la déconnexion
        return "redirect:/";
    }




}
