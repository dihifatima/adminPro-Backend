package com.example.security.entity;

import com.example.security.Authentification.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandeService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDemande;

    @ManyToOne
    private User client;

    @ManyToOne
    private Service service;

    private String etat = "en attente";
    private java.time.LocalDateTime dateSoumission;
    private java.time.LocalDateTime dateRDV;
}
