package com.example.security.entity;

import com.example.security.Authentification.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;



@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Demendes_Services")

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

    @LastModifiedDate
    private LocalDateTime dateRendezvous;

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
