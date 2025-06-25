package com.example.security.service.facade;

import com.example.security.entity.Creneau;
import com.example.security.entity.CreneauDisponibilite;
import com.example.security.service.impl.CapaciteManagementServiceImpl.CapaciteApplicationStrategy;
import com.example.security.service.impl.CapaciteManagementServiceImpl.CapaciteChangeReport;

import java.time.LocalDate;

public interface CapaciteManagementService {

    /**
     * Modifie la capacité d'un créneau spécifique
     * @param creneauId ID du créneau à modifier
     * @param nouvelleCapacite Nouvelle capacité
     * @return Créneau mis à jour
     */
    Creneau updateCapaciteSpecifiqueCreneau(Long creneauId, Integer nouvelleCapacite);

    /**
     * Modifie la capacité par défaut avec stratégie d'application
     * @param creneauDisponibiliteId ID du modèle de créneau
     * @param nouvelleCapacite Nouvelle capacité par défaut
     * @param strategy Stratégie d'application aux créneaux existants
     * @param dateDebut Date à partir de laquelle appliquer (optionnel)
     * @return CreneauDisponibilite mis à jour
     */
    CreneauDisponibilite updateCapaciteAvecChoixApplication(
            Long creneauDisponibiliteId,
            Integer nouvelleCapacite,
            CapaciteApplicationStrategy strategy,
            LocalDate dateDebut
    );

    /**
     * Remet un créneau à sa capacité par défaut
     * @param creneauId ID du créneau
     * @return Créneau mis à jour
     */
    Creneau resetCapaciteParDefaut(Long creneauId);

    /**
     * Analyse l'impact d'un changement de capacité
     * @param creneauDisponibiliteId ID du modèle
     * @param nouvelleCapacite Nouvelle capacité proposée
     * @param dateDebut Date de début d'analyse
     * @return Rapport d'impact
     */
    CapaciteChangeReport analyserImpactChangementCapacite(
            Long creneauDisponibiliteId,
            Integer nouvelleCapacite,
            LocalDate dateDebut
    );
}