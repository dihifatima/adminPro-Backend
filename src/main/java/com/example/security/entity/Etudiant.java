package com.example.security.entity;

import com.example.security.Authentification.user.User;
import jakarta.persistence.*;
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
@DiscriminatorValue("Etudiant")
public class Etudiant extends User {
    private String codeMassar;           // Code Massar unique
    private String codeCIN;              // Numéro de la carte d'identité nationale
    private LocalDate dateNaissance;     // Date de naissance
    private String lieuNaissance;        // Lieu de naissance
    private String adresse;              // Adresse complète
    private String genre;                // Homme / Femme
    private String nationalite;          // Exemple : Marocaine
    private String niveauScolaire;       // Exemple : Bac+2, Licence, Master 1
    private String mentionBac;// Exemple : Bien, Très Bien// Nom de l'établissement actuel
    private String typeBac;              // Exemple : Bac Sciences Maths, Bac SVT
    private String anneeBac;             // Année d’obtention du bac

}
