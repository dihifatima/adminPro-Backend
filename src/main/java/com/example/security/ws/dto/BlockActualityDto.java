package com.example.security.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlockActualityDto {
    private Long id;
    private String titre;
    private String slug;
    private String auteur;
    private String categorie;

    private String imageUrl;
    private String imageName;

    private LocalDateTime datePublication;
    private String statut; // BROUILLON, PUBLIE, ARCHIVE

    private Set<String> tags;

    // === Contenu structuré ===
    private String introduction;

    private String titre1;
    private String section1;

    private String titre2;
    private String section2;

    private String titre3;
    private String section3;

    private String titre4;
    private String section4;

    private String conclusion;

    // Statistiques
    private Integer viewCount;
}
