package com.example.security.dao;
import com.example.security.entity.Creneau;
import com.example.security.entity.DemandeService;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DemandeServiceRepository extends JpaRepository<DemandeService, Long> {
    DemandeService findByRef(String ref);

    long countByCreneauAndStatutIn(Creneau creneau, List<String> enAttente);
}