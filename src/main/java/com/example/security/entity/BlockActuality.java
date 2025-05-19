package com.example.security.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
@Data
@Entity
@Table(name = "block_actuality")
public class BlockActuality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private String slug;
    private String auteur;

    @Column(nullable = false)
    private String categorie;

    private String imageUrl;               // URL d’accès à l’image (frontend)
    private String imageName;

    // Nom du fichier physique (stocké dans /uploads/)
    private LocalDateTime datePublication;

    @Column(nullable = false)
    private String statut; // BROUILLON, PUBLIE, ARCHIVE


    @ElementCollection
    @CollectionTable(
            name = "block_actuality_tags",
            joinColumns = @JoinColumn(name = "actuality_id")
    )
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();


    // === Nouveaux attributs ===
    @Column(name = "introduction", columnDefinition = "TEXT")
    private String introduction;          // Section Introduction
    private String titre1;
    @Column(name = "section1", columnDefinition = "TEXT")
    private String section1;

    private String titre2;
    @Column(name = "section2", columnDefinition = "TEXT")
    private String section2;


    private String titre3;
    @Column(name = "section3", columnDefinition = "TEXT")
    private String section3;

    private String titre4;
    @Column(name = "section4", columnDefinition = "TEXT")
    private String section4;

    @Column(name = "conclusion", columnDefinition = "TEXT")
    private String conclusion;

    // Nombre de vues pour des statistiques
    private Integer viewCount = 0;
    // Méthode pour incrémenter le compteur de vues
    public void incrementViewCount() {
        this.viewCount = (this.viewCount == null) ? 1 : this.viewCount + 1;
    }
}
