package com.example.security.ws.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PdfDto {
    private String ref;
    private Long userId;
    private Long serviceOffertId;

    // Noms cohérents avec le controller et le template
    private String userNom;           // au lieu de nomClient
    private String email;             // au lieu de emailClient
    private String telephone;         // au lieu de telephoneClient
    private String serviceOffertNom;  // au lieu de nomServiceOffert
    private String creneau;           // au lieu de creneauDate
    private LocalDate dateCreneau;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private LocalDateTime dateSoumission;
}