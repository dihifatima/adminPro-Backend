package com.example.security.service.facade;

import com.example.security.entity.DemandeService;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

public interface DemandeServiceService {
    DemandeService saveAvecCreneau(DemandeService demande, Long creneauId);

    // Méthodes de base CRUD
    DemandeService save(DemandeService demande);
    DemandeService update(DemandeService demande);
    DemandeService findById(Long id);
    DemandeService findByRef(String ref);
    List<DemandeService> findAll();
    int deleteById(Long id);
    int deleteByRef(String ref);

    // Gestion des statuts
    DemandeService updateStatut(String ref, String nouveauStatut);
    DemandeService accepterDemande(String ref, Long creneauId);
    DemandeService refuserDemande(String ref, String motifRefus);
    DemandeService annulerDemande(String ref);

    // Recherches spécifiques
    List<DemandeService> findByUserFullName(String userNom);
    List<DemandeService> findByServiceOffertNom(String serviceOffertNom);
    List<DemandeService> findByStatut(String statut);
    List<DemandeService> findByUser(Long userId);
    List<DemandeService> findByDateRange(LocalDate dateDebut, LocalDate dateFin);
    List<DemandeService> findDemandesEnAttente();
    List<DemandeService> findDemandesAcceptees();
    List<DemandeService> findDemandesRefusees();

    // Gestion des créneaux et dates
    List<LocalDateTime> findAllReservedDates();
    List<DemandeService> findByDateRendezvous(LocalDate date);
    boolean isDateDisponible(LocalDateTime dateRendezvous);
    boolean hasDemandeForUserAndService(String userEmail, String serviceName);

    // Statistiques et rapports
    long countDemandesByStatut(String statut);
    long countDemandesByUser(Long userId);
    long countDemandesByService(Long serviceId);
    List<DemandeService> findRecentDemandes(int nombreJours);

    // Validation
    void validateDemande(DemandeService demande);
    boolean canUserCreateDemande(String userEmail, String serviceName);
}