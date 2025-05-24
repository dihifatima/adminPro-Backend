package com.example.security.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@EqualsAndHashCode(exclude = {"blockActuality"})
@ToString(exclude = {"blockActuality"})
@NoArgsConstructor
@AllArgsConstructor
public class BlogSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    // Annotation pour gérer la référence circulaire
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actuality_id")
    private BlockActuality blockActuality;
}