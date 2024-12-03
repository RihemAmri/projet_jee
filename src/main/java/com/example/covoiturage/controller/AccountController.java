package com.example.covoiturage.controller;

import com.example.covoiturage.entity.AppUser;
import com.example.covoiturage.entity.RegisterDto;
import com.example.covoiturage.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController {

    @Autowired
    private UserRepository repo;

    // Méthode pour afficher le formulaire d'inscription
    @GetMapping("/register")
    public String register(Model model) {
        RegisterDto registerDto = new RegisterDto();
        model.addAttribute("registerDto", registerDto);  // Ajout du DTO à la vue
        model.addAttribute("success", false);  // État de succès (initialement false)
        return "register";
    }

    // Méthode pour traiter l'inscription (POST)
    @PostMapping("/register")
    public String handleRegister(

            Model model,
            @Valid @ModelAttribute RegisterDto registerDto,
            BindingResult result) {
        System.out.println("offffffffffffffffffff");
        System.out.println(registerDto);
        // Vérifie si l'email est déjà utilisé
        AppUser appUser = repo.findByEmail(registerDto.getEmail());
        if (appUser != null) {
            result.addError(new FieldError("registerDto", "email", "Email address is already used"));
        }

        // Si des erreurs de validation existent, retourne à la page d'inscription
        if (result.hasErrors()) {
            return "register";
        }

        try {
            BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
            AppUser newUser = new AppUser();
            System.out.println("helooooooo");
            System.out.println(registerDto.getFirstName());
            newUser.setFirstName(registerDto.getFirstName());
            newUser.setLastName(registerDto.getLastName());
            newUser.setEmail(registerDto.getEmail());
            newUser.setRole(registerDto.getRole());
            newUser.setPhoneNumber(registerDto.getPhoneNumber());
            newUser.setPassword(bcryptEncoder.encode(registerDto.getPassword()));
            repo.save(newUser);

            model.addAttribute("registerDto", new RegisterDto());
            model.addAttribute("success", true);
            return "register";

        } catch (Exception e) {
            System.out.println("Error saving user: " + e.getMessage());  // Log d'erreur
            result.addError(new FieldError("registerDto", "firstName", e.getMessage()));
        }


        return "register";  // Retourne à la page d'inscription
    }

}