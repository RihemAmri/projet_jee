package com.example.covoiturage.controller;

import com.example.covoiturage.entity.AppUser;
import com.example.covoiturage.entity.Ride;
import com.example.covoiturage.repository.RideRepository;
import com.example.covoiturage.service.RideService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RideController {

    private final RideService rideService;
    private final RideRepository rideRepository;

    // Constructeur
    public RideController(RideService rideService, RideRepository rideRepository) {
        this.rideService = rideService;
        this.rideRepository = rideRepository;
    }

    // Convertir LocalDate en Date pour la recherche
    private Date convertToDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    @GetMapping("/addrides")
    public String showAddRideForm(Model model) {
        model.addAttribute("ride", new Ride());
        return "addrides"; // La page Thymeleaf (addRide.html)
    }

    @PostMapping("/addrides")
    public String saveRide(@ModelAttribute Ride ride, HttpSession session) {
        System.out.println(ride.getDriver());
        try {
            // Récupérer l'ID de l'utilisateur depuis la session
            Long userId = (Long) session.getAttribute("id");


            if (userId == null) {
                // Si l'utilisateur n'est pas connecté, rediriger vers la page de login
                System.out.println("hiii");
                return "redirect:/loginn";
            }

            // Ici, vous pouvez récupérer l'utilisateur et lui attribuer la course
            System.out.println(userId);

            ride.setDriver(new AppUser(userId)); // Associer l'utilisateur à la course (ajustez selon votre logique)
            rideService.saveRide(ride);

            return "redirect:/Myrides"; // Rediriger vers la liste des courses après l'ajout
        } catch (Exception e) {
            e.printStackTrace();
            return "error"; // Afficher une page d'erreur en cas de problème
        }
    }
    @GetMapping("/api/rides")
    @ResponseBody
    public List<Ride> getAllRidesApi() {
        return rideService.getAllRides();
    }

    // Méthode pour afficher tous les trajets
    @GetMapping("/rides")
    public String getAllRides(HttpSession session, Model model) {
        // Récupérer les informations de la session
        String role = (String) session.getAttribute("role"); // récupérer le rôle de l'utilisateur depuis la session
        model.addAttribute("role", role);

        // Obtenir la date et l'heure actuelles
        Date now = new Date();

        // Récupérer tous les trajets et filtrer ceux avec une date de départ future
        List<Ride> rides = rideService.getAllRides().stream()
                .filter(ride -> ride.getDepartureDate().after(now)) // Vérifiez que getDepartureDate retourne un java.util.Date
                .collect(Collectors.toList());

        // Ajouter les trajets filtrés au modèle
        model.addAttribute("rides", rides);

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
        String role = (String) session.getAttribute("role");
        model.addAttribute("role", role);

        // Obtenir la date et l'heure actuelles comme un objet Date
        Date now = new Date();

        // Convertir departureDate en java.util.Date si nécessaire
        Date dateForSearch = null;
        if (departureDate != null) {
            dateForSearch = Date.from(departureDate.atZone(ZoneId.systemDefault()).toInstant());
            // Validation pour vérifier que la date de départ est future
            if (dateForSearch.before(now)) {
                model.addAttribute("error", "La date de départ doit être dans le futur.");
                return "rides";
            }
        }

        // Rechercher les trajets en fonction des critères
        List<Ride> rides = rideService.searchRides(departurePoint, destination, dateForSearch, maxPrice, driverName);

        // Filtrer les trajets avec une date de départ future
        rides = rides.stream()
                .filter(ride -> ride.getDepartureDate().after(now)) // Vérifiez que getDepartureDate retourne java.util.Date
                .collect(Collectors.toList());

        // Ajouter les résultats et critères au modèle
        model.addAttribute("rides", rides);
        model.addAttribute("departurePoint", departurePoint);
        model.addAttribute("destination", destination);
        model.addAttribute("departureDate", departureDate);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("driverName", driverName);

        return "rides";
    }



    @GetMapping("/Myrides")
    public String getUserRides(Model model, HttpSession session) {
        // Récupérer l'ID utilisateur depuis la session
        Long userId = (Long) session.getAttribute("id");

        // Vérifier si l'utilisateur est connecté
        if (userId == null) {
            return "redirect:/loginn"; // Redirigez vers la page de connexion si l'utilisateur n'est pas connecté
        }

        // Charger les rides de l'utilisateur connecté à partir du repository
        List<Ride> userRides = rideRepository.findByDriverId(userId); // Remplacer par votre méthode de recherche

        // Ajouter les rides dans le modèle pour être utilisées dans la vue Thymeleaf
        model.addAttribute("rides", userRides);

        // Retourner le nom de la vue Thymeleaf (Myrides.html)
        return "Myrides";
    }
    @GetMapping("rides/edit/{id}")
    public String editRide(@PathVariable("id") Long id, Model model) {
        Ride ride = rideService.getRideById(id);
        if (ride == null) {
            System.out.println("Ride not found for ID: " + id);
            return "redirect:/rides";
        }
        System.out.println("Editing Ride ID: " + id);
        model.addAttribute("ride", ride);
        return "edit";
    }

    // Mettre à jour un ride
    @PostMapping("/rides/edit/{id}")
    public String updateRide(@PathVariable Long id, @ModelAttribute Ride ride) {
        // Récupérer la version existante de la base de données
        Ride existingRide = rideService.findRideById(id);
        if (existingRide == null) {
            throw new IllegalArgumentException("Ride not found for id: " + id);
        }

        // Mise à jour conditionnelle des champs
        if (!existingRide.getDeparturePoint().equals(ride.getDeparturePoint())) {
            existingRide.setDeparturePoint(ride.getDeparturePoint());
        }
        if (!existingRide.getDestination().equals(ride.getDestination())) {
            existingRide.setDestination(ride.getDestination());
        }
        if (!existingRide.getDepartureDate().equals(ride.getDepartureDate())) {
            existingRide.setDepartureDate(ride.getDepartureDate());
        }
        if (existingRide.getAvailableSeats() != ride.getAvailableSeats()) {
            existingRide.setAvailableSeats(ride.getAvailableSeats());
        }
        if (existingRide.getPricePerSeat() != ride.getPricePerSeat()) {
            existingRide.setPricePerSeat(ride.getPricePerSeat());
        }
        if (!existingRide.getComments().equals(ride.getComments())) {
            existingRide.setComments(ride.getComments());
        }

        // Appeler le service pour sauvegarder les modifications
        rideService.updateRide(existingRide);

        return "redirect:/Myrides";
    }

    /*@GetMapping("/rides/delete/{id}")
    public String deleteRide(@PathVariable Long id) {
        Ride existingRide = rideService.findRideById(id);
        rideService.delete(existingRide);
        return "redirect:/Myrides"; // Redirige vers la liste des rides
    }*/

    @GetMapping("/rides/delete/{id}")
    public String deleteRide(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            rideService.deleteRideAndNotify(id);
            redirectAttributes.addFlashAttribute("successMessage", "Ride deleted successfully, and notifications have been sent.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while deleting the ride: " + e.getMessage());
        }
        return "redirect:/Myrides";
    }
    @GetMapping("/map")
    public String showMapPage(Model model) {
        // Get all rides from the service
        List<Ride> rides = rideService.getAllRides(); // Fetch all rides from the database

        // Add rides to the model to make them available in the Thymeleaf template
        model.addAttribute("rides", rides);

        return "map"; // Return the view "map.html"
    }
    @PostMapping("/update-coordinates")
    public ResponseEntity<String> updateCoordinates() {
        rideService.updateCoordinatesForAllRides();
        return ResponseEntity.ok("Coordinates updated successfully");
    }


}

