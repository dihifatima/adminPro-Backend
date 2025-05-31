package com.example.security.ws.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandeServiceDto {

    private Long id;
    private String ref;
    private Long userId;
    private String userNom;
    private String telephone;
    private Long serviceOffertId;
    private String serviceOffertNom;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateSoumission;
    private String statut = "en attent";
    // Champ pour le créneau
    private Long creneau;
    private LocalDate dateCreneau;
    private LocalTime heureDebut;
    private LocalTime heureFin;


}