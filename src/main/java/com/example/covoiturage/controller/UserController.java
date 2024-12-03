package com.example.covoiturage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @GetMapping({"", "/"})
    public String home(Model model, Authentication authentication) {
        String role = ""; // Valeur par défaut pour le rôle

        try {
            // Vérification de l'authentification
            if (authentication != null) {
                logger.info("Authentication object is not null");

                // Récupération du rôle (premier rôle trouvé)
                role = authentication.getAuthorities().stream()
                        .map(authority -> authority.getAuthority())
                        .findFirst()
                        .orElse(""); // Si aucun rôle n'est trouvé, renvoie une chaîne vide

                logger.info("Role after finding authority: " + role);

                // Suppression du préfixe 'ROLE_' du rôle
                role = role.replace("ROLE_", "");

                logger.info("Role after removing 'ROLE_' prefix: " + role);
            } else {
                logger.info("Authentication object is null");
            }

            // Ajout du rôle au modèle pour la vue
            model.addAttribute("role", role);

            // Retour de la vue 'about'
            return "about";

        } catch (Exception e) {
            // Enregistrer l'exception dans les logs
            logger.error("An error occurred while processing the role: ", e);

            // Ajouter un message d'erreur au modèle (facultatif)
            model.addAttribute("errorMessage", "An error occurred. Please try again later.");

            // Retour d'une page d'erreur (par exemple, "error")
            return "error"; // Page d'erreur
        }
    }



    @GetMapping({"/login",})
    public String login(){
        return "login";
    }


}
