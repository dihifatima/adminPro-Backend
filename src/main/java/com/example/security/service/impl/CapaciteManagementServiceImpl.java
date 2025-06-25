package com.example.security.service.impl;

import com.example.security.dao.CreneauDisponibiliteRepository;
import com.example.security.dao.CreneauxRepository;
import com.example.security.dao.DemandeServiceRepository;
import com.example.security.entity.Creneau;
import com.example.security.entity.CreneauDisponibilite;
import com.example.security.service.facade.CapaciteManagementService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CapaciteManagementServiceImpl implements CapaciteManagementService {

    private final CreneauxRepository creneauxRepository;
    private final CreneauDisponibiliteRepository creneauDisponibiliteRepository;
    private final DemandeServiceRepository demandeServiceRepository;

    public CapaciteManagementServiceImpl(CreneauxRepository creneauxRepository,
                                         CreneauDisponibiliteRepository creneauDisponibiliteRepository,
                                         DemandeServiceRepository demandeServiceRepository) {
        this.creneauxRepository = creneauxRepository;
        this.creneauDisponibiliteRepository = creneauDisponibiliteRepository;
        this.demandeServiceRepository = demandeServiceRepository;
    }

    /**
     * Modifie la capacité d'un créneau spécifique (par date)
     */
    @Override
    public Creneau updateCapaciteSpecifiqueCreneau(Long creneauId, Integer nouvelleCapacite) {
        if (nouvelleCapacite == null || nouvelleCapacite <= 0) {
            throw new IllegalArgumentException("La nouvelle capacité doit être supérieure à 0");
        }

        Creneau creneau = creneauxRepository.findById(creneauId)
                .orElseThrow(() -> new RuntimeException("Créneau non trouvé avec l'ID : " + creneauId));

        // Calculer le nombre de places déjà réservées
        int placesReservees = getPlacesReservees(creneau);

        // Vérifier que la nouvelle capacité permet les réservations existantes
        if (nouvelleCapacite < placesReservees) {
            throw new IllegalArgumentException(
                    String.format("Impossible de réduire la capacité à %d. Il y a déjà %d réservations confirmées.",
                            nouvelleCapacite, placesReservees)
            );
        }

        // Mettre à jour la capacité et recalculer la capacité restante
        int nouvelleCapaciteMax = nouvelleCapacite;
        int nouvelleCapaciteRestante = nouvelleCapaciteMax - placesReservees;

        // Marquer ce créneau comme ayant une capacité personnalisée
        creneau.setCapaciteRestante(nouvelleCapaciteRestante);
        creneau.setCapacitePersonnalisee(nouvelleCapaciteMax); // Nouveau champ à ajouter
        creneau.setCapaciteHeritee(false); // Nouveau champ à ajouter

        return creneauxRepository.save(creneau);
    }



    /**
     * Modifie la capacité par défaut ET applique aux créneaux futurs non personnalisés
     */
    @Override
    public CreneauDisponibilite updateCapaciteAvecChoixApplication(
            Long creneauDisponibiliteId,
            Integer nouvelleCapacite,
            CapaciteApplicationStrategy strategy,
            LocalDate dateDebut) {

        if (nouvelleCapacite == null || nouvelleCapacite <= 0) {
            throw new IllegalArgumentException("La nouvelle capacité doit être supérieure à 0");
        }

        CreneauDisponibilite creneauDispo = creneauDisponibiliteRepository.findById(creneauDisponibiliteId)
                .orElseThrow(() -> new RuntimeException("CreneauDisponibilite non trouvé"));

        // Mettre à jour la capacité par défaut
        creneauDispo.setCapaciteMax(nouvelleCapacite);
        CreneauDisponibilite saved = creneauDisponibiliteRepository.save(creneauDispo);

        // Appliquer selon la stratégie choisie
        switch (strategy) {
            case NOUVEAUX_CRENEAUX_SEULEMENT:
                // Ne pas toucher aux créneaux existants
                break;

            case TOUS_CRENEAUX_FUTURS_SANS_RESERVATION:
                appliquerCapaciteAuxCreneauxSansReservation(creneauDisponibiliteId, nouvelleCapacite, dateDebut);
                break;

            case TOUS_CRENEAUX_FUTURS_AVEC_VERIFICATION:
                appliquerCapaciteAvecVerification(creneauDisponibiliteId, nouvelleCapacite, dateDebut);
                break;

            case CRENEAUX_NON_PERSONNALISES:
                appliquerCapaciteAuxCreneauxNonPersonnalises(creneauDisponibiliteId, nouvelleCapacite, dateDebut);
                break;
        }

        return saved;
    }

    /**
     * Remet un créneau à sa capacité par défaut (héritée du CreneauDisponibilite)
     */
    @Override
    public Creneau resetCapaciteParDefaut(Long creneauId) {
        Creneau creneau = creneauxRepository.findById(creneauId)
                .orElseThrow(() -> new RuntimeException("Créneau non trouvé"));

        CreneauDisponibilite creneauDispo = creneau.getCreneauDisponibilite();
        if (creneauDispo == null) {
            throw new RuntimeException("Ce créneau n'est pas lié à un CreneauDisponibilite");
        }

        int placesReservees = getPlacesReservees(creneau);
        int capaciteParDefaut = creneauDispo.getCapaciteMax();

        if (capaciteParDefaut < placesReservees) {
            throw new IllegalArgumentException(
                    String.format("Impossible de revenir à la capacité par défaut (%d). Il y a %d réservations.",
                            capaciteParDefaut, placesReservees)
            );
        }

        // Remettre aux valeurs par défaut
        creneau.setCapaciteRestante(capaciteParDefaut - placesReservees);
        creneau.setCapacitePersonnalisee(null);
        creneau.setCapaciteHeritee(true);

        return creneauxRepository.save(creneau);
    }

    /**
     * Obtient un rapport détaillé sur l'impact d'un changement de capacité
     */
    @Override
    public CapaciteChangeReport analyserImpactChangementCapacite(
            Long creneauDisponibiliteId,
            Integer nouvelleCapacite,
            LocalDate dateDebut) {

        List<Creneau> creneauxConcernes = creneauxRepository
                .findByCreneauDisponibiliteIdAndDateCreneauAfter(creneauDisponibiliteId, dateDebut != null ? dateDebut : LocalDate.now());

        CapaciteChangeReport report = new CapaciteChangeReport();

        for (Creneau creneau : creneauxConcernes) {
            int placesReservees = getPlacesReservees(creneau);

            if (nouvelleCapacite < placesReservees) {
                report.addCreneauProblematique(creneau, placesReservees, nouvelleCapacite);
            } else {
                report.addCreneauOk(creneau, placesReservees, nouvelleCapacite);
            }
        }

        return report;
    }

    // ============ MÉTHODES PRIVÉES ============

    private int getPlacesReservees(Creneau creneau) {
        // Compter les demandes confirmées pour ce créneau
        return Math.toIntExact(demandeServiceRepository.countByCreneauAndStatutIn(
                creneau, List.of("EN_ATTENTE", "CONFIRME", "TRAITE")
        ));
    }

    private void appliquerCapaciteAuxCreneauxSansReservation(Long creneauDisponibiliteId, Integer nouvelleCapacite, LocalDate dateDebut) {
        LocalDate startDate = dateDebut != null ? dateDebut : LocalDate.now();

        List<Creneau> creneauxSansReservation = creneauxRepository
                .findByCreneauDisponibiliteIdAndDateCreneauAfter(creneauDisponibiliteId, startDate)
                .stream()
                .filter(c -> getPlacesReservees(c) == 0)
                .collect(Collectors.toList());

        for (Creneau creneau : creneauxSansReservation) {
            creneau.setCapaciteRestante(nouvelleCapacite);
            creneau.setCapacitePersonnalisee(null);
            creneau.setCapaciteHeritee(true);
        }

        creneauxRepository.saveAll(creneauxSansReservation);
    }

    private void appliquerCapaciteAvecVerification(Long creneauDisponibiliteId, Integer nouvelleCapacite, LocalDate dateDebut) {
        LocalDate startDate = dateDebut != null ? dateDebut : LocalDate.now();

        List<Creneau> creneauxFuturs = creneauxRepository
                .findByCreneauDisponibiliteIdAndDateCreneauAfter(creneauDisponibiliteId, startDate);

        List<Creneau> creneauxModifiables = creneauxFuturs.stream()
                .filter(c -> getPlacesReservees(c) <= nouvelleCapacite)
                .collect(Collectors.toList());

        for (Creneau creneau : creneauxModifiables) {
            int placesReservees = getPlacesReservees(creneau);
            creneau.setCapaciteRestante(nouvelleCapacite - placesReservees);
            creneau.setCapacitePersonnalisee(null);
            creneau.setCapaciteHeritee(true);
        }

        creneauxRepository.saveAll(creneauxModifiables);

        // Log des créneaux qui n'ont pas pu être modifiés
        List<Creneau> creneauxNonModifiables = creneauxFuturs.stream()
                .filter(c -> getPlacesReservees(c) > nouvelleCapacite)
                .collect(Collectors.toList());

        if (!creneauxNonModifiables.isEmpty()) {
            System.out.println("ATTENTION: " + creneauxNonModifiables.size() +
                    " créneaux n'ont pas pu être modifiés car ils ont trop de réservations");
        }
    }

    private void appliquerCapaciteAuxCreneauxNonPersonnalises(Long creneauDisponibiliteId, Integer nouvelleCapacite, LocalDate dateDebut) {
        LocalDate startDate = dateDebut != null ? dateDebut : LocalDate.now();

        List<Creneau> creneauxNonPersonnalises = creneauxRepository
                .findByCreneauDisponibiliteIdAndDateCreneauAfter(creneauDisponibiliteId, startDate)
                .stream()
                .filter(c -> c.getCapaciteHeritee() == null || c.getCapaciteHeritee()) // Créneaux non personnalisés
                .filter(c -> getPlacesReservees(c) <= nouvelleCapacite) // Vérification de sécurité
                .collect(Collectors.toList());

        for (Creneau creneau : creneauxNonPersonnalises) {
            int placesReservees = getPlacesReservees(creneau);
            creneau.setCapaciteRestante(nouvelleCapacite - placesReservees);
        }

        creneauxRepository.saveAll(creneauxNonPersonnalises);
    }

    // ============ CLASSES UTILITAIRES ============

    public enum CapaciteApplicationStrategy {
        NOUVEAUX_CRENEAUX_SEULEMENT,           // Appliquer seulement aux nouveaux créneaux générés
        TOUS_CRENEAUX_FUTURS_SANS_RESERVATION, // Appliquer à tous les créneaux futurs sans réservation
        TOUS_CRENEAUX_FUTURS_AVEC_VERIFICATION, // Appliquer avec vérification (skip si trop de réservations)
        CRENEAUX_NON_PERSONNALISES             // Appliquer seulement aux créneaux non personnalisés
    }

    public static class CapaciteChangeReport {
        private List<CreneauImpact> creneauxProblematiques = new ArrayList<>();
        private List<CreneauImpact> creneauxOk = new ArrayList<>();

        public void addCreneauProblematique(Creneau creneau, int reservations, int nouvelleCapacite) {
            creneauxProblematiques.add(new CreneauImpact(creneau, reservations, nouvelleCapacite));
        }

        public void addCreneauOk(Creneau creneau, int reservations, int nouvelleCapacite) {
            creneauxOk.add(new CreneauImpact(creneau, reservations, nouvelleCapacite));
        }

        // Getters...
        public List<CreneauImpact> getCreneauxProblematiques() { return creneauxProblematiques; }
        public List<CreneauImpact> getCreneauxOk() { return creneauxOk; }
        public boolean hasProblems() { return !creneauxProblematiques.isEmpty(); }
    }

    public static class CreneauImpact {
        private final Creneau creneau;
        private final int reservationsExistantes;
        private final int nouvelleCapacite;

        public CreneauImpact(Creneau creneau, int reservationsExistantes, int nouvelleCapacite) {
            this.creneau = creneau;
            this.reservationsExistantes = reservationsExistantes;
            this.nouvelleCapacite = nouvelleCapacite;
        }

        // Getters...
        public Creneau getCreneau() { return creneau; }
        public int getReservationsExistantes() { return reservationsExistantes; }
        public int getNouvelleCapacite() { return nouvelleCapacite; }
    }
}