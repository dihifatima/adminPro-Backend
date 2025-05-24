package com.example.security.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"sections"})
@ToString(exclude = {"sections"})
@Entity
@Table(name = "block_actuality")
public class BlockActuality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String slug;
    private String auteur;

    @Column(name = "introduction", columnDefinition = "TEXT")
    private String introduction;

    @Column(nullable = false)
    private String categorie;


    private String imageUrl;
    private LocalDateTime datePublication;

    @Column(nullable = false)
    private String statut; // BROUILLON, PUBLIE, ARCHIVE


    @Column(name = "conclusion", columnDefinition = "TEXT")
    private String conclusion;

    @ElementCollection
    @CollectionTable(
            name = "block_actuality_tags",
            joinColumns = @JoinColumn(name = "actuality_id")
    )
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();

    // Annotation pour gérer la référence circulaire
    @JsonManagedReference
    @OneToMany(mappedBy = "blockActuality", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BlogSection> sections = new HashSet<>();

    private Integer viewCount = 0;

    public void incrementViewCount() {
        this.viewCount = (this.viewCount == null) ? 1 : this.viewCount + 1;
    }
}