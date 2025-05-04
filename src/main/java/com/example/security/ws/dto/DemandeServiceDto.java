package com.example.security.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data  // Annotation Lombok pour générer automatiquement getters, setters, equals, hashCode et toString
@NoArgsConstructor
@AllArgsConstructor
public class DemandeServiceDto {
    private Long idDemande;
    private Long userId;  // ID de l'utilisateur qui fait la demande
    private String userEmail; // Email de l'utilisateur (pour faciliter l'affichage)
    private String userName;  // Nom complet de l'utilisateur (pour faciliter l'affichage)

    private Long serviceId; // ID du service demandé
    private String serviceName; // Nom du service (pour faciliter l'affichage)

    private String etat;    // Représenté en String dans le DTO

    private LocalDateTime dateSoumission;
    private LocalDateTime dateRDV;
}
