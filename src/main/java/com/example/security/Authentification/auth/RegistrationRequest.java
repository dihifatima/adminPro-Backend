package com.example.security.Authentification.auth;


import jakarta.persistence.Entity;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {

    private boolean isAdmin;
    private boolean isEtudiant;
    @AssertTrue(message = "C'est soit un admin, soit un étudiant (pas les deux, ni aucun).")
    private boolean isExactlyOneRole() {
        return isAdmin ^ isEtudiant; // XOR : un seul des deux doit être vrai
    }

    private String  niveauEtude;
    private String filiere;
    private String etablissementActuel;
    private  String adminDepartement;
    // Nom de l'établissement actuel
    // Ces champs doivent être des byte[] ou String pour stocker le contenu des fichiers
    // ou des chemins d'accès aux fichiers
    private byte[] scanBac; // Contenu binaire du scan du Bac
    private byte[] cinScan; // Contenu binaire du scan de la CIN
    private byte[] releveDeNotesScan;

    @NotEmpty(message = "Firstname is mandatory")
    @NotNull(message = "Firstname is mandatory")
    private String firstname;
    @NotEmpty(message = "Lastname is mandatory")
    @NotNull(message = "Lastname is mandatory")
    private String lastname;
    @Email(message = "Email is not well formatted")
    @NotEmpty(message = "Email is mandatory")
    @NotNull(message = "Email is mandatory")
    private String email;
    @NotEmpty(message = "Password is mandatory")
    @NotNull(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String password;

    // Validation de la taille des fichiers pour éviter les fichiers trop grands
    @AssertTrue(message = "Le fichier du Bac ne doit pas dépasser 5 Mo.")
    private boolean isScanBacSizeValid() {
        return scanBac == null || scanBac.length <= 5 * 1024 * 1024; // 5 Mo max
    }

    @AssertTrue(message = "Le fichier CIN ne doit pas dépasser 5 Mo.")
    private boolean isCinScanSizeValid() {
        return cinScan == null || cinScan.length <= 5 * 1024 * 1024; // 5 Mo max
    }

    @AssertTrue(message = "Le fichier de relevé de notes ne doit pas dépasser 5 Mo.")
    private boolean isReleveDeNotesScanSizeValid() {
        return releveDeNotesScan == null || releveDeNotesScan.length <= 5 * 1024 * 1024; // 5 Mo max
    }



}
