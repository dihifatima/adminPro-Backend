package com.example.security.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlocActualites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    @Column(length = 500)
    private String resume;

    @Lob
    private String contenu;

    private String auteur;

    private String categorie;

    private String imageUrl;

    private String slug;

    @CreatedDate
    private LocalDateTime datePublication;

    // Getters et Setters
}
