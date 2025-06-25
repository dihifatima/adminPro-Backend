package com.example.security.dao;
import com.example.security.entity.Creneau;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CreneauxRepository extends JpaRepository<Creneau, Long> {
    boolean existsByDateCreneauAndHeureDebutAndHeureFin(LocalDate date, LocalTime heureDebut, LocalTime heureFin);
    List<Creneau> findByDateCreneauAfterAndCapaciteRestanteGreaterThan(LocalDate aujourdhui, int i);
    List<Creneau> findByDateCreneauBefore(LocalDate dateLimit);

    List<Creneau> findByCreneauDisponibiliteIdAndDateCreneauAfter(Long creneauDisponibiliteId, LocalDate date);

    List<Creneau> findByActifTrueAndCapaciteRestanteGreaterThan(int i);

    List<Creneau> findByDateCreneauAndActifTrue(LocalDate date);
}