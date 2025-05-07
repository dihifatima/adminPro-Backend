package com.example.security.entity;

import com.example.security.Authentification.user.User;
import jakarta.persistence.*;
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
    private String ScanBacPath;
    private String CinScanPath;
    private String Photos;
    private String ReleveDeNotesScanPath;




}
