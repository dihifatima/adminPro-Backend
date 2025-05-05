package com.example.security.entity;

import com.example.security.Authentification.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;



@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Demandes_Services")


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

@EntityListeners(AuditingEntityListener.class)
public class DemandeService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime dateSoumission;


    private LocalDateTime dateRendezvous;

    // ✅ Nouveau : nom de l'utilisateur (copié de User)
    private String userNom;

    // ✅ Nouveau : nom du service (copié de ServiceOffert)
    private String serviceOffertNom;
    // ✅ Un utilisateur fait plusieurs demandes
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // ✅ Une demande est liée à un seul service
    @ManyToOne
    @JoinColumn(name = "service_offert_id")
    private ServiceOffert serviceOffert;

    // ✅ Statut : en attente, traité...
    private String statut; // Ex : "EN_ATTENTE", "TRAITÉ"
}
