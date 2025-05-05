package com.example.security.service.facade;


import com.example.security.entity.DemandeService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DemandeServiceService  {
    // Sauvegarder une nouvelle demande de service avec serviceOffert et user associés

    DemandeService save(DemandeService demandeService, Long serviceOffertId, Long userId, String userNom, String serviceOffertNom, LocalDateTime dateRendezvous, String statut);

    // Trouver une demande de service par son ID
    Optional<DemandeService> findById(Long id);

    // Récupérer toutes les demandes de service
    List<DemandeService> findAll();
    // Mettre à jour le statut d'une demande de service
    DemandeService updateStatut(Long id, String nouveauStatut);

    // Supprimer une demande de service par son ID
    void deleteById(Long id);

    // Récupérer les demandes de service par utilisateur
    List<DemandeService> findByUserId(Long userId);

    // Récupérer les demandes de service par service offert
    List<DemandeService> findByServiceOffertId(Long serviceOffertId);
}
