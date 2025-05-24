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
    private String introduction;
    private String slug;
    private String auteur;
    private String categorie;
    private String imageUrl;

    private LocalDateTime datePublication;
    private String statut;
    private String conclusion;

    private Set<String> tags;

    private Set<BlogSectionDTO> sections = new HashSet<>();

    private Integer viewCount;
}