package com.example.security.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data  // Annotation Lombok pour générer automatiquement getters, setters, equals, hashCode et toString
@NoArgsConstructor
@AllArgsConstructor
public class EntrepreneurDto {
    // Champs hérités de User
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String telephone;

    private String cin;         // Nom de l'entreprise
    private String nomEntreprise;         // Nom de l'entreprise
    private String secteurActivite;       // Domaine d'activité
    private String registreCommerce;      // Numéro RC
    private String identifiantFiscal;     // Numéro IF
    private String typeEntreprise;        // SARL, SAS, Auto-entrepreneur
    private LocalDate dateCreation;       // Date de création de l'entreprise
    private String siegeSocial;
}
