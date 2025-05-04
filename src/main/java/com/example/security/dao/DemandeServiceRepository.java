package com.example.security.dao;

import com.example.security.Authentification.user.User;
import com.example.security.entity.DemandeService;
import com.example.security.entity.EtatDemande;
import com.example.security.entity.ServiceOffert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DemandeServiceRepository extends JpaRepository<DemandeService, Long> {
    // Trouver toutes les demandes d'un utilisateur
    List<DemandeService> findByClient(User client);

    // Trouver toutes les demandes pour un service spécifique
    List<DemandeService> findByServiceOffert(ServiceOffert serviceOffert);

    // Trouver les demandes par état
    List<DemandeService> findByEtat(EtatDemande etat);

    // Trouver les demandes entre deux dates
    List<DemandeService> findByDateSoumissionBetween(LocalDateTime debut, LocalDateTime fin);

    // Trouver les demandes d'un utilisateur avec un état spécifique
    List<DemandeService> findByClientAndEtat(User client, EtatDemande etat);

    // Trouver les demandes les plus récentes (avec pagination via JPQL)
    @Query("SELECT d FROM DemandeService d ORDER BY d.dateSoumission DESC")
    List<DemandeService> findRecentDemandes();

    // Compter le nombre de demandes par état
    @Query("SELECT COUNT(d) FROM DemandeService d WHERE d.etat = :etat")
    Long countByEtat(@Param("etat") EtatDemande etat);
}
