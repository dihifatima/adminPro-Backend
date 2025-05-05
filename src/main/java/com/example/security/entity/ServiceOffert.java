package com.example.security.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)

@Builder
public class ServiceOffert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedAt;

    // Relation bidirectionnelle avec DemandeService
    //si tu supprimes un ServiceOffert, il faudra s'assurer que toutes les demandes associées à ce service sont également supprimées ou mises à jour. Tu peux utiliser l'option CascadeType.ALL ou gérer la suppression dans le service
    @OneToMany(mappedBy = "serviceOffert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DemandeService> demandes;
}


