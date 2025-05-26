package com.example.security.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
@Entity
@Table(name = "creneaux_disponibilite")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreneauDisponibilite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "jour_semaine")
    private DayOfWeek jourSemaine; // Lundi, Mardi, ...

    @Column(name = "heure_debut")
    private LocalTime heureDebut;

    @Column(name = "heure_fin")
    private LocalTime heureFin;

    @Column(name = "capacite_max")
    private Integer capaciteMax = 4;  // max clients par créneau

    @Column(name = "duree_minutes")
    private Integer dureeMinutes = 60;

    @Column(name = "actif")
    private Boolean actif = true;

    @Column(name = "cree_par_admin")
    private Boolean creeParAdmin = false;
}
