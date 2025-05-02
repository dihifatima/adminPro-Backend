package com.example.security.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // Annotation Lombok pour générer automatiquement getters, setters, equals, hashCode et toString
@NoArgsConstructor
@AllArgsConstructor
public class EtudiantDto {
    // Champs hérités de User
    private Long id;
    private String firstname;
    private String lastname;
    private String email;

    // Champs spécifiques à Etudiant
    private String niveauEtude;
    private String filiere;
    private String etablissementActuel;
    private String scanBacPath;
    private String cinScanPath;
    private String photos;
    private String releveDeNotesScanPath;

}