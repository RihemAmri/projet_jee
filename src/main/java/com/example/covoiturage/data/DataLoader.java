/*package com.example.covoiturage.data;

import com.example.covoiturage.entity.Ride;
import com.example.covoiturage.entity.User;
import com.example.covoiturage.repository.RideRepository;
import com.example.covoiturage.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component  // L'annotation @Component permet à Spring de la détecter automatiquement
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RideRepository rideRepository;

    public DataLoader(UserRepository userRepository, RideRepository rideRepository) {
        this.userRepository = userRepository;
        this.rideRepository = rideRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Créer des utilisateurs de test
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("password123");
        user1.setPhoneNumber("1234567890");
        user1.setRole("driver");

        User user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setEmail("jane.doe@example.com");
        user2.setPassword("password123");
        user2.setPhoneNumber("0987654321");
        user2.setRole("passenger");

        userRepository.save(user1);
        userRepository.save(user2);

        // Créer des trajets de test
        Ride ride1 = new Ride();
        ride1.setDriver(user1);  // Associer l'utilisateur John comme conducteur
        ride1.setDeparturePoint("Paris");
        ride1.setDestination("Lyon");
        ride1.setDepartureDate(new Date());
        ride1.setAvailableSeats(3);
        ride1.setPricePerSeat(25.0);
        ride1.setComments("Trajet confortable");
        ride1.setRestrictions("Non-fumeur");

        Ride ride2 = new Ride();
        ride2.setDriver(user1);  // Associer l'utilisateur John comme conducteur
        ride2.setDeparturePoint("Marseille");
        ride2.setDestination("Nice");
        ride2.setDepartureDate(new Date());
        ride2.setAvailableSeats(2);
        ride2.setPricePerSeat(20.0);
        ride2.setComments("Trajet rapide");
        ride2.setRestrictions("Pas de bagages volumineux");

        rideRepository.save(ride1);
        rideRepository.save(ride2);

        System.out.println("Données de test insérées");
    }
}*/
