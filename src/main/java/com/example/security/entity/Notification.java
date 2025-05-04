package com.example.security.entity;

import com.example.security.Authentification.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNotification;

    @ManyToOne
    @JoinColumn(name = "destinataire_id", nullable = false)
    private User destinataire;

    private String sujet;
    private String message;
    private LocalDateTime dateCreation;
    private boolean vue = false;

    @ManyToOne
    @JoinColumn(name = "demande_id")
    private DemandeService demandeService;  // Référence optionnelle à une demande
}