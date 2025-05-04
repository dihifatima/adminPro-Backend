package com.example.security.service.facade;

import com.example.security.Authentification.user.User;
import com.example.security.entity.DemandeService;
import com.example.security.entity.EtatDemande;

import java.time.LocalDateTime;
import java.util.List;

public interface DemandeServiceService {
    // Créer une nouvelle demande de service
    DemandeService create(DemandeService demandeService);

    // Mettre à jour l'état d'une demande
    DemandeService updateStatus(Long idDemande, EtatDemande nouvelEtat);

    // Planifier un RDV pour une demande
    DemandeService planifierRDV(Long idDemande, LocalDateTime dateRDV);

    // Rechercher une demande par ID
    DemandeService findById(Long id);

    // Récupérer toutes les demandes
    List<DemandeService> findAll();

    // Récupérer les demandes d'un utilisateur
    List<DemandeService> findByUser(User user);

    // Récupérer les demandes par état
    List<DemandeService> findByEtat(EtatDemande etat);

    // Récupérer les demandes entre deux dates
    List<DemandeService> findByDateSoumissionBetween(LocalDateTime debut, LocalDateTime fin);

    // Récupérer les demandes récentes
    List<DemandeService> findRecentDemandes();

    // Supprimer une demande
    int delete(Long id);

    // Statistiques
    Long countByEtat(EtatDemande etat);
}
