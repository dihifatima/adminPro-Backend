package com.example.security.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogSectionDTO {
    private Long id;
    private String titre;        // Optionnel : pour les titres
    private String contenu;      // Le texte du paragraphe
}