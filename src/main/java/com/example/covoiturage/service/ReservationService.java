package com.example.covoiturage.service;

import com.example.covoiturage.entity.Reservation;

public interface ReservationService {
    void saveReservation(Reservation reservation);
    void deleteReservation(Long id);
    public Reservation findById(Long id);

}
