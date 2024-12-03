package com.example.covoiturage.repository;

import com.example.covoiturage.entity.AppUser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

       AppUser findByEmail(String email);}