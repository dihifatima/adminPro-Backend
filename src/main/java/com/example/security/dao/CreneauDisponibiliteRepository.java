package com.example.security.dao;

import com.example.security.entity.CreneauDisponibilite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.util.List;

public interface CreneauDisponibiliteRepository extends JpaRepository<CreneauDisponibilite, Long> {

    // Trouver tous les créneaux actifs
    List<CreneauDisponibilite> findByActifTrue();

    // Trouver par jour de la semaine et actif
    List<CreneauDisponibilite> findByJourSemaineAndActifTrue(DayOfWeek jourSemaine);

    // Trouver les créneaux créés par admin
    List<CreneauDisponibilite> findByCreeParAdminTrue();

    // Trouver les créneaux par défaut (non créés par admin)
    List<CreneauDisponibilite> findByCreeParAdminFalse();

    // Vérifier s'il existe des créneaux personnalisés pour un jour
    @Query("SELECT COUNT(cd) > 0 FROM CreneauDisponibilite cd WHERE cd.jourSemaine = :jour AND cd.creeParAdmin = true AND cd.actif = true")
    boolean existsCustomCreneauForDay(@Param("jour") DayOfWeek jour);

    // Obtenir tous les jours de la semaine configurés
    @Query("SELECT DISTINCT cd.jourSemaine FROM CreneauDisponibilite cd WHERE cd.actif = true ORDER BY cd.jourSemaine")
    List<DayOfWeek> findAllConfiguredDays();

    long countByActifTrue();

    List<CreneauDisponibilite> findByJourSemaine(DayOfWeek jourSemaine);
}