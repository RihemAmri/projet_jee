package com.example.covoiturage.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BcryptGenerator {

    private PasswordEncoder passwordEncoder;

    public BcryptGenerator() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Méthode pour encoder un mot de passe
    public String passwordEncoder(String password) {
        return passwordEncoder.encode(password);
    }

    // Méthode pour vérifier un mot de passe
    public boolean passwordDecoder(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
