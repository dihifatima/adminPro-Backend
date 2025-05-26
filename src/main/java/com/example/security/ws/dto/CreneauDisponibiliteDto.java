package com.example.security.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
@Data  // Annotation Lombok pour générer automatiquement getters, setters, equals, hashCode et toString
@NoArgsConstructor
@AllArgsConstructor
public class CreneauDisponibiliteDto {
    private Long id;
    private DayOfWeek jourSemaine; // Lundi, Mardi, ...
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private Integer capaciteMax;
    private Integer dureeMinutes;
    private Boolean actif;
    private Boolean creeParAdmin;
}
