package com.example.security.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data  // Annotation Lombok pour générer automatiquement getters, setters, equals, hashCode et toString
@NoArgsConstructor
@AllArgsConstructor
public class PorteVisaDto {
    // Champs hérités de User
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String telephone;

    private String codeCIN;              // Carte d'identité nationale
    private String passportNumber;       // Numéro de passeport
    private LocalDate dateDelivrancePassport;
    private LocalDate dateExpirationPassport;
    private LocalDate dateNaissance;
    private String lieuNaissance;
    private String nationalite;
    private String adresse;              // Adresse de résidence
    private String genre;
    private String destinationVisa;// Pays demandé
    private String typeVisa;          // Tourisme, Études, Travail, etc
    private String dureeSejour;          // courte  long

}
