package com.example.security.Controllers;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecuperationEtudiantResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String role;
    private String niveauEtude;
    private String filiere;
    private String etablissementActuel; // Nom de l'établissement actuel
    private String scanBac; // Lien vers le scan du Bac (PDF ou Image)
    private String cinScan; // Lien vers le scan de la CIN (PDF ou Image)
    private String releveDeNotesScan;

}
