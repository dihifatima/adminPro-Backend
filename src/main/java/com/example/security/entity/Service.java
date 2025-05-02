package com.example.security.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idService;
    private String nom;
    private String description;
    @Enumerated(EnumType.STRING)
    private EtatDemande type;
    private String lienFormulaire;
    private boolean estDisponible;
}
