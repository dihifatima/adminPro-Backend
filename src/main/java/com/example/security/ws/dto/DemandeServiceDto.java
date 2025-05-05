package com.example.security.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data  // Annotation Lombok pour générer automatiquement getters, setters, equals, hashCode et toString
@NoArgsConstructor
@AllArgsConstructor
public class DemandeServiceDto {
    private Long id;
    private LocalDateTime dateSoumission;
    private LocalDateTime dateRendezvous;
    private String statut;
    private Long userId;
    private Long serviceOffertId;
}
