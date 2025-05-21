package com.example.security.ws.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data  // Annotation Lombok pour générer automatiquement getters, setters, equals, hashCode et toString
@NoArgsConstructor
@AllArgsConstructor
public class EtudiantDto {
    // Champs hérités de User
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String telephone;

    // Champs spécifiques à Etudiant
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
    private String anneeBac;

}