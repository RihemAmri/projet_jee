package com.example.covoiturage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Retourne le fichier login.html dans le dossier templates
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String username, @RequestParam String password, Model model) {
        // Logique de vérification des identifiants
        if ("admin".equals(username) && "password".equals(password)) {
            return "redirect:/"; // Exemple de redirection vers le tableau de bord
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }
}

