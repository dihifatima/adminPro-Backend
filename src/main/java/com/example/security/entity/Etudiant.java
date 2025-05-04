package com.example.security.entity;

import com.example.security.Authentification.user.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("Etudiant")
public class Etudiant extends User {
    private String  niveauEtude;
    private String filiere;
    private String etablissementActuel; // Nom de l'établissement actuel
    private String scanBacPath;           // Ex: "uploads/scan_bac.pdf"
    private String cinScanPath;           // Ex: "uploads/cin_scan.jpg"
    private String photos;                // Ex: "uploads/photo.jpg"
    private String releveDeNotesScanPath; // Lien vers le relevé de notes (PDF ou Image)

}
