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
    private Integer capaciteRestante;

    // 🆕 NOUVEAUX CHAMPS pour la gestion de capacité
    @Column(name = "capacite_personnalisee")
    private Integer capacitePersonnalisee; // Si non null, ce créneau a une capacité spécifique

    @Column(name = "capacite_heritee")
    private Boolean capaciteHeritee = true; // True si hérite du CreneauDisponibilite

    @Column(name = "actif")
    private Boolean actif = true;

    @OneToMany(mappedBy = "creneau")
    private List<DemandeService> demandes;

    // 🆕 MÉTHODES UTILITAIRES

    /**
     * Retourne la capacité maximale effective de ce créneau
     */
    public Integer getCapaciteMaxEffective() {
        if (capacitePersonnalisee != null && !capaciteHeritee) {
            return capacitePersonnalisee;
        }
        return creneauDisponibilite != null ? creneauDisponibilite.getCapaciteMax() : 0;
    }

    /**
     * Vérifie si ce créneau a une capacité personnalisée
     */
    public boolean hasCapacitePersonnalisee() {
        return capacitePersonnalisee != null && !capaciteHeritee;
    }

    /**
     * Retourne le nombre de places occupées
     */
    public Integer getPlacesOccupees() {
        Integer capaciteMax = getCapaciteMaxEffective();
        return capaciteMax - (capaciteRestante != null ? capaciteRestante : 0);
    }

    /**
     * Vérifie si le créneau est disponible pour réservation
     */
    public boolean isDisponiblePourReservation() {
        return actif &&
                capaciteRestante != null &&
                capaciteRestante > 0 &&
                creneauDisponibilite != null &&
                creneauDisponibilite.getActif();
    }

    /**
     * Met à jour la capacité en mode personnalisé
     */
    public void setCapacitePersonnaliseeMode(Integer nouvelleCapacite) {
        this.capacitePersonnalisee = nouvelleCapacite;
        this.capaciteHeritee = false;
        // Recalculer la capacité restante
        Integer placesOccupees = getPlacesOccupees();
        this.capaciteRestante = nouvelleCapacite - placesOccupees;
    }

    /**
     * Remet en mode hérité (capacité du CreneauDisponibilite)
     */
    public void resetToHeritedCapacity() {
        this.capacitePersonnalisee = null;
        this.capaciteHeritee = true;
        // Recalculer avec la capacité du parent
        if (creneauDisponibilite != null) {
            Integer placesOccupees = getPlacesOccupees();
            this.capaciteRestante = creneauDisponibilite.getCapaciteMax() - placesOccupees;
        }
    }
}