package com.example.security.service.facade;


import com.example.security.entity.DemandeService;

import java.time.LocalDateTime;
import java.util.List;

public interface DemandeServiceService  {

    DemandeService save(DemandeService demandeService );
    DemandeService updateStatut(String ref, String nouveauStatut);
    DemandeService findByRef(String ref);
    int deleteByRef(String ref);
    List<DemandeService> findAll();
    // Recherche des demandes par le nom de l'utilisateur
    List<DemandeService> findByUserFullName(String userNom);

    // Recherche des demandes par le nom du service offert
    List<DemandeService> findByServiceOffertNom(String serviceOffertNom);

    List<LocalDateTime> findAllReservedDates();
}
