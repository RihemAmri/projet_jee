package com.example.covoiturage.service;

import com.example.covoiturage.entity.Reservation;
import com.example.covoiturage.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public void saveReservation(Reservation reservation) {
        reservationRepository.save(reservation); // Utilise le repository pour sauvegarder la réservation
    }
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
    @Override
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElse(null); // Retourne null si aucune réservation n'est trouvée
    }

}
