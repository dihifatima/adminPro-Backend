package com.example.security.dao;
import com.example.security.entity.CreneauDisponibilite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.DayOfWeek;
import java.util.List;

public interface CreneauDisponibiliteRepository extends JpaRepository<CreneauDisponibilite, Long> {
    List<CreneauDisponibilite> findByActifTrue();
    List<CreneauDisponibilite> findByJourSemaineAndActifTrue(DayOfWeek jourSemaine);
    long countByActifTrue();
    List<CreneauDisponibilite> findByJourSemaine(DayOfWeek jourSemaine);
}