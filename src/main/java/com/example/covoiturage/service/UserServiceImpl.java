package com.example.covoiturage.service;
import com.example.covoiturage.entity.AppUser;
import com.example.covoiturage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;
    public AppUser findById(Long id) {
        return userRepository.findById(id).orElse(null); // Retourne null si l'utilisateur n'est pas trouvé
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Retrieve the user from the repository
        AppUser appuser = userRepository.findByEmail(email);

        // Debugging log to check if the user was found
        if (appuser == null) {
            System.out.println("User not found with email: " + email);
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Logging appuser details (if needed)
        System.out.println("User found: " + appuser.toString());

        // Construct Spring Security User with username, password, and role
        return User.withUsername(appuser.getEmail())
                .password(appuser.getPassword())
                .roles(appuser.getRole().toUpperCase()) // Ensure role is uppercase
                .build();
    }

}