package com.example.security.entity;

import com.example.security.Authentification.user.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("PorteVisa")

public class PorteVisa extends User {
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
