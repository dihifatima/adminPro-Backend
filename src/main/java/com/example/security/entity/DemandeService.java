package com.example.security.entity;

import com.example.security.Authentification.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandeService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDemande;


    @ManyToOne
    @JoinColumn(
            name = "user_id",nullable = false
    )
    private User  client;

    @ManyToOne
    @JoinColumn(
            name = "service_id",nullable = false
    )

    private ServiceOffert serviceOffert ;

    @Enumerated(EnumType.STRING)
    private EtatDemande etat = EtatDemande.EN_ATTENTE;

    @PrePersist
    protected void onCreate() {
        this.dateSoumission = LocalDateTime.now();
    }
    private LocalDateTime dateSoumission ;
//Chaque fois que tu vas créer une nouvelle DemandeService, la date de soumission (dateSoumission) sera automatiquement remplie avec la date et l'heure actuelles sans que tu aies besoin de la définir manuellement dans le code ou dans une requête HTTP.
    private LocalDateTime dateRDV;
}
