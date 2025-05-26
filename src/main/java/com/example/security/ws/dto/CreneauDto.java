package com.example.security.ws.dto;

import com.example.security.entity.CreneauDisponibilite;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
@Data  // Annotation Lombok pour générer automatiquement getters, setters, equals, hashCode et toString
@NoArgsConstructor
@AllArgsConstructor
public class CreneauDto {
    private Long id;
    private CreneauDisponibilite creneauDisponibilite;
    private LocalDate dateCreneau;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private Integer capaciteRestante;
    private Boolean actif;
}
