package com.example.covoiturage.repository;

import com.example.covoiturage.entity.AppUser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {

       AppUser findByEmail(String email);
}