package com.example.security.ws.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreneauDto {
    private Long id;
    private LocalDate dateCreneau;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private Integer capaciteRestante;
    private Boolean actif;
    private Long creneauDisponibiliteId; // ID seulement si tu ne veux pas tout l'objet
    private List<Long> demandes; // Liste d'IDs des DemandeService associés

}
