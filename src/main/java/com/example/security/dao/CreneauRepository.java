package com.example.security.dao;

import com.example.security.entity.Creneau;
import com.example.security.entity.CreneauDisponibilite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface CreneauRepository extends JpaRepository<Creneau, Long> {

    // Trouver tous les créneaux actifs
    List<Creneau> findByActifTrue();

    // Trouver les créneaux par date
    List<Creneau> findByDateCreneauAndActifTrue(LocalDate dateCreneau);

    // Trouver un créneau spécifique par date et heure
    Optional<Creneau> findByDateCreneauAndHeureDebutAndHeureFinAndActifTrue(
            LocalDate dateCreneau, LocalTime heureDebut, LocalTime heureFin);

    // Trouver les créneaux disponibles (capacité restante > 0)
    @Query("SELECT c FROM Creneau c WHERE c.dateCreneau = :date AND c.capaciteRestante > 0 AND c.actif = true ORDER BY c.heureDebut")
    List<Creneau> findAvailableCreneauxByDate(@Param("date") LocalDate date);

    // Trouver les créneaux par plage de dates
    @Query("SELECT c FROM Creneau c WHERE c.dateCreneau BETWEEN :dateDebut AND :dateFin AND c.actif = true ORDER BY c.dateCreneau, c.heureDebut")
    List<Creneau> findByDateRange(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

    // Compter le nombre de réservations pour un créneau
    @Query("SELECT COUNT(ds) FROM DemandeService ds WHERE ds.creneau.id = :creneauId")
    int countReservationsByCreneau(@Param("creneauId") Long creneauId);

    // Trouver les créneaux basés sur CreneauDisponibilite
    List<Creneau> findByCreneauDisponibilite(CreneauDisponibilite creneauDisponibilite);

    // Vérifier si un créneau existe déjà pour une date et CreneauDisponibilite donnés
    boolean existsByDateCreneauAndCreneauDisponibilite(LocalDate dateCreneau, CreneauDisponibilite creneauDisponibilite);

    // Trouver les créneaux futurs disponibles
    @Query("SELECT c FROM Creneau c WHERE c.dateCreneau >= :today AND c.capaciteRestante > 0 AND c.actif = true ORDER BY c.dateCreneau, c.heureDebut")
    List<Creneau> findFutureAvailableCreneaux(@Param("today") LocalDate today);

    List<Creneau> findByDateCreneauAndActifTrueAndCapaciteRestanteGreaterThan(LocalDate date, int i);

    boolean existsByDateCreneauAndHeureDebutAndHeureFin(LocalDate date, LocalTime heureDebut, LocalTime heureFin);

    void deleteByDateCreneauAfterAndCapaciteRestante(LocalDate aujourdhui, int i);

    List<Creneau> findByCreneauDisponibiliteIdAndDateCreneauAfter(Long creneauDisponibiliteId, LocalDate aujourdhui);

    void deleteByDateCreneauBefore(LocalDate hierDate);

    List<Creneau> findByDateCreneauAfterAndCapaciteRestanteGreaterThan(LocalDate aujourdhui, int i);

    List<Creneau> findByDateCreneauBefore(LocalDate dateLimit);
}