package com.example.security.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data  // Annotation Lombok pour générer automatiquement getters, setters, equals, hashCode et toString
@NoArgsConstructor
@AllArgsConstructor
public class BlocActualitesDto {
    private Long id;
    private String titre;
    private String resume;
    private String contenu;
    private String auteur;
    private String categorie;
    private String imageUrl;
    private String slug;
    private LocalDateTime datePublication;
}
