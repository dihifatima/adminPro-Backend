package com.example.security.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // Annotation Lombok pour générer automatiquement getters, setters, equals, hashCode et toString
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOffertDto {
    private String nom;
    private String description;
    private String type; // car TypeService est un enum, on l'expose en String dans le DTO
    private boolean estDisponible;
}
