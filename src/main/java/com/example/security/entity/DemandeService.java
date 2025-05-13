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
   private  String ref;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime dateSoumission;
    private LocalDateTime dateRendezvous;
    @ManyToOne
    //c’est la table demande_service qui contient la colonne user_id pour indiquer à quel utilisateur appartient chaque demande.
    @JoinColumn(name = "user_id")
    private User user;
    //  Une demande est liée à un seul service
    @ManyToOne
    @JoinColumn(name = "service_offert_id")
    private ServiceOffert serviceOffert;
    // Statut : en attente, traité...
    private String statut; // Ex : "EN_ATTENTE", "TRAITÉ"
}
