package com.example.security.dao;
import com.example.security.entity.Creneau;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CreneauRepository extends JpaRepository<Creneau, Long> {
    List<Creneau> findByDateCreneauAndActifTrue(LocalDate dateCreneau);
    List<Creneau> findByDateCreneauAndActifTrueAndCapaciteRestanteGreaterThan(LocalDate date, int i);
    boolean existsByDateCreneauAndHeureDebutAndHeureFin(LocalDate date, LocalTime heureDebut, LocalTime heureFin);
    List<Creneau> findByCreneauDisponibiliteIdAndDateCreneauAfter(Long creneauDisponibiliteId, LocalDate aujourdhui);
    List<Creneau> findByDateCreneauAfterAndCapaciteRestanteGreaterThan(LocalDate aujourdhui, int i);
    List<Creneau> findByDateCreneauBefore(LocalDate dateLimit);

}