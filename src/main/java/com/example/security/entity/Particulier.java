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
@DiscriminatorValue("Particulier")
public class Particulier extends User {
    private String objetDemande;           // Objet du service demandé
    private String ville;                  // Ville de résidence
    private String adresse;                // Adresse complète
    private String codeCIN;                // Code de la carte d'identité
    private String lieuNaissance;          // Facultatif selon contexte
    private String nationalite;
    private String genre;                  // Homme / Femme
    private LocalDate dateNaissance;
}
