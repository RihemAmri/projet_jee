package com.example.covoiturage.controller;

import com.example.covoiturage.entity.Ride;
import com.example.covoiturage.service.RideService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Controller
public class RideController {

    private final RideService rideService;

    // Constructeur
    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    // Convertir LocalDate en Date pour la recherche
    private Date convertToDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    // Méthode pour afficher tous les trajets
    @GetMapping("/rides")
    public String getAllRides(HttpSession session, Model model) {
        // Récupérer les informations de la session
        String role = (String) session.getAttribute("role");  // récupérer le rôle de l'utilisateur depuis la session

        // Ajouter le rôle au modèle pour l'afficher dans la page
        model.addAttribute("role", role);

        // Récupérer tous les trajets
        List<Ride> rides = rideService.getAllRides(); // Récupérer tous les trajets
        model.addAttribute("rides", rides); // Ajouter les trajets au modèle

        return "rides"; // Retourner la vue "rides"
    }

    // Méthode pour rechercher les trajets selon les critères
    @GetMapping("/rides/search")
    public String searchRides(
            @RequestParam(required = false) String departurePoint,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureDate,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String driverName,
            HttpSession session, Model model) {

        // Récupérer les informations de la session
        String role = (String) session.getAttribute("role");  // récupérer le rôle de l'utilisateur depuis la session

        // Ajouter le rôle au modèle pour l'afficher dans la page
        model.addAttribute("role", role);

        // Convertir LocalDateTime en Date
        Date dateForSearch = convertToDate(departureDate);

        // Rechercher les trajets en fonction des critères
        List<Ride> rides = rideService.searchRides(departurePoint, destination, dateForSearch, maxPrice, driverName);

        // Ajouter les résultats de recherche et les critères de recherche au modèle
        model.addAttribute("rides", rides);
        model.addAttribute("departurePoint", departurePoint);
        model.addAttribute("destination", destination);
        model.addAttribute("departureDate", departureDate);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("driverName", driverName);

        return "rides"; // Retourner la vue "rides"
    }

    // Convertir LocalDateTime en Date
    private Date convertToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
