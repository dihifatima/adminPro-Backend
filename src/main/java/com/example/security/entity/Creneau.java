package com.example.security.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "creneaux")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Creneau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //plusieurs créneaux peuvent venir du même modèle de disponibilité
    @ManyToOne
    @JoinColumn(name = "creneau_disponibilite_id")
    private CreneauDisponibilite creneauDisponibilite;

    @Column(name = "date_creneau")
    private LocalDate dateCreneau;

    @Column(name = "heure_debut")
    private LocalTime heureDebut;

    @Column(name = "heure_fin")
    private LocalTime heureFin;

    @Column(name = "capacite_restante")
    private Integer capaciteRestante ;

    @Column(name = "actif")
    private Boolean actif = true;

    @OneToMany(mappedBy = "creneau")
    private List<DemandeService> demandes;
}