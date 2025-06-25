package com.example.security.service.facade;

import com.example.security.entity.Creneau;
import java.time.LocalDate;
import java.util.List;

public interface CreneauService {

    // Méthodes existantes
    Creneau findById(Long id);
    List<Creneau> findAll();
    boolean isCreneauAvailable(Long creneauId);
    Creneau updateCreneauStatus(Long creneauId, Boolean actif);
    List<Creneau> findAvailableCreneauxForBooking();
    Creneau reserverCreneau(Long creneauId);

    // Nouvelles méthodes pour la gestion des créneaux passés
    void cleanupPassedCreneaux();
    void cleanupOldCreneaux(int daysOld);
    Creneau reactiverCreneau(Long creneauId);
    long countAvailableCreneauxForDate(LocalDate date);
    List<Creneau> findAvailableCreneauxForDate(LocalDate date);
}