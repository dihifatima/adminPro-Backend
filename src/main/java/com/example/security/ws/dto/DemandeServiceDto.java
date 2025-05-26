package com.example.security.ws.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandeServiceDto {

    private Long id;
    private String ref;
    private Long userId;
    private String userNom;
    private Long serviceOffertId;
    private String serviceOffertNom;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateSoumission;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateRendezvous;

    private String statut;

    // Champ pour le créneau
    private Long creneau;
}