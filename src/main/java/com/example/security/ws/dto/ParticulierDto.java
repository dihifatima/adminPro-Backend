package com.example.security.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data  // Annotation Lombok pour générer automatiquement getters, setters, equals, hashCode et toString
@NoArgsConstructor
@AllArgsConstructor
public class ParticulierDto {
    // Champs hérités de User
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String telephone;

    private String objetDemande;           // Objet du service demandé
    private String ville;                  // Ville de résidence
    private String adresse;                // Adresse complète
    private String codeCIN;                // Code de la carte d'identité
    private String lieuNaissance;          // Facultatif selon contexte
    private String nationalite;
    private String genre;                  // Homme / Femme
    private LocalDate dateNaissance;
}
