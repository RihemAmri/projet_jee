package com.example.covoiturage.service;

import com.example.covoiturage.entity.AppUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface UserService {
    AppUser findById(Long id);

    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    void updateUser(AppUser user);
}