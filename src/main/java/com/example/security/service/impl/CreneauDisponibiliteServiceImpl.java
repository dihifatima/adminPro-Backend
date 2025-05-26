package com.example.security.service.impl;

import com.example.security.dao.CreneauDisponibiliteRepository;
import com.example.security.entity.CreneauDisponibilite;
import com.example.security.service.facade.CreneauDisponibiliteService;
import com.example.security.service.facade.CreneauGenerationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CreneauDisponibiliteServiceImpl implements CreneauDisponibiliteService {

    // Correction : enlever @Autowired du final field
    private final CreneauDisponibiliteRepository creneauDisponibiliteRepository;

    // Injection optionnelle pour éviter les dépendances circulaires
    @Autowired(required = false)
    private CreneauGenerationService creneauGenerationService;

    public CreneauDisponibiliteServiceImpl(CreneauDisponibiliteRepository creneauDisponibiliteRepository) {
        this.creneauDisponibiliteRepository = creneauDisponibiliteRepository;
    }

    @Override
    public CreneauDisponibilite save(CreneauDisponibilite creneauDisponibilite) {
        validateCreneauDisponibilite(creneauDisponibilite);

        // Vérifier les chevauchements avant la sauvegarde
        if (hasTimeConflict(creneauDisponibilite)) {
            throw new IllegalArgumentException(
                    "Un créneau existe déjà pour ce jour et ces horaires : " +
                            creneauDisponibilite.getJourSemaine() + " " +
                            creneauDisponibilite.getHeureDebut() + "-" + creneauDisponibilite.getHeureFin()
            );
        }

        CreneauDisponibilite saved = creneauDisponibiliteRepository.save(creneauDisponibilite);

        // Régénérer les créneaux concrets si un service de génération est disponible
        if (creneauGenerationService != null) {
            creneauGenerationService.updateCreneauxAfterDisponibiliteChange(saved.getId());
        }

        return saved;
    }

    @Override
    public CreneauDisponibilite update(CreneauDisponibilite creneauDisponibilite) {
        if (creneauDisponibilite == null || creneauDisponibilite.getId() == null) {
            throw new IllegalArgumentException("CreneauDisponibilite ou son ID ne peut pas être null");
        }

        // Vérifier que l'entité existe
        CreneauDisponibilite existing = findById(creneauDisponibilite.getId());

        // Valider les nouvelles données
        validateCreneauDisponibilite(creneauDisponibilite);

        // Vérifier les conflits horaires (en excluant l'entité actuelle)
        if (hasTimeConflictExcluding(creneauDisponibilite, creneauDisponibilite.getId())) {
            throw new IllegalArgumentException(
                    "Un autre créneau existe déjà pour ce jour et ces horaires : " +
                            creneauDisponibilite.getJourSemaine() + " " +
                            creneauDisponibilite.getHeureDebut() + "-" + creneauDisponibilite.getHeureFin()
            );
        }

        CreneauDisponibilite updated = creneauDisponibiliteRepository.save(creneauDisponibilite);

        // Mettre à jour les créneaux concrets associés
        if (creneauGenerationService != null) {
            creneauGenerationService.updateCreneauxAfterDisponibiliteChange(updated.getId());
        }

        return updated;
    }

    @Override
    public CreneauDisponibilite findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }
        return creneauDisponibiliteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CreneauDisponibilite non trouvé avec l'ID : " + id));
    }

    @Override
    public List<CreneauDisponibilite> findAll() {
        return creneauDisponibiliteRepository.findAll();
    }

    @Override
    public List<CreneauDisponibilite> findAllActive() {
        return creneauDisponibiliteRepository.findByActifTrue();
    }

    @Override
    public List<CreneauDisponibilite> findByJourSemaine(DayOfWeek jourSemaine) {
        if (jourSemaine == null) {
            throw new IllegalArgumentException("Le jour de la semaine ne peut pas être null");
        }
        return creneauDisponibiliteRepository.findByJourSemaineAndActifTrue(jourSemaine);
    }

    @Override
    public int deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        CreneauDisponibilite creneauDisponibilite = findById(id);

        // Vérifier si des créneaux concrets sont liés et ont des réservations
        // Cette logique pourrait être ajoutée selon vos besoins métier

        creneauDisponibiliteRepository.delete(creneauDisponibilite);

        // Optionnel : nettoyer les créneaux concrets associés
        if (creneauGenerationService != null) {
            creneauGenerationService.synchronizeCreneauxWithDisponibilite();
        }

        return 1;
    }

    @Override
    public CreneauDisponibilite activate(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        CreneauDisponibilite creneauDisponibilite = findById(id);
        creneauDisponibilite.setActif(true);
        CreneauDisponibilite activated = creneauDisponibiliteRepository.save(creneauDisponibilite);

        // Synchroniser les créneaux concrets
        if (creneauGenerationService != null) {
            creneauGenerationService.updateCreneauxAfterDisponibiliteChange(id);
        }

        return activated;
    }

    @Override
    public CreneauDisponibilite deactivate(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        CreneauDisponibilite creneauDisponibilite = findById(id);
        creneauDisponibilite.setActif(false);
        CreneauDisponibilite deactivated = creneauDisponibiliteRepository.save(creneauDisponibilite);

        // Synchroniser les créneaux concrets
        if (creneauGenerationService != null) {
            creneauGenerationService.updateCreneauxAfterDisponibiliteChange(id);
        }

        return deactivated;
    }

    // ========== MÉTHODES UTILITAIRES ET VALIDATIONS ==========

    /**
     * Valide les données d'un créneau de disponibilité
     */
    private void validateCreneauDisponibilite(CreneauDisponibilite creneauDisponibilite) {
        if (creneauDisponibilite == null) {
            throw new IllegalArgumentException("CreneauDisponibilite ne peut pas être null");
        }

        if (creneauDisponibilite.getJourSemaine() == null) {
            throw new IllegalArgumentException("Le jour de la semaine est obligatoire");
        }

        if (creneauDisponibilite.getHeureDebut() == null) {
            throw new IllegalArgumentException("L'heure de début est obligatoire");
        }

        if (creneauDisponibilite.getHeureFin() == null) {
            throw new IllegalArgumentException("L'heure de fin est obligatoire");
        }

        if (creneauDisponibilite.getHeureDebut().isAfter(creneauDisponibilite.getHeureFin())) {
            throw new IllegalArgumentException("L'heure de début doit être antérieure à l'heure de fin");
        }

        if (creneauDisponibilite.getHeureDebut().equals(creneauDisponibilite.getHeureFin())) {
            throw new IllegalArgumentException("L'heure de début et de fin ne peuvent pas être identiques");
        }

        if (creneauDisponibilite.getCapaciteMax() == null || creneauDisponibilite.getCapaciteMax() <= 0) {
            throw new IllegalArgumentException("La capacité maximale doit être supérieure à 0");
        }

        if (creneauDisponibilite.getDureeMinutes() == null || creneauDisponibilite.getDureeMinutes() <= 0) {
            throw new IllegalArgumentException("La durée en minutes doit être supérieure à 0");
        }

        // Validation des horaires d'ouverture (selon vos règles métier)
        validateBusinessHours(creneauDisponibilite);
    }

    /**
     * Valide que le créneau respecte les horaires d'ouverture
     */
    private void validateBusinessHours(CreneauDisponibilite creneau) {
        LocalTime heureDebut = creneau.getHeureDebut();
        LocalTime heureFin = creneau.getHeureFin();
        DayOfWeek jour = creneau.getJourSemaine();

        // Horaires d'ouverture selon vos spécifications
        switch (jour) {
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY:
                // Lundi-Vendredi : 9h-13h et 14h-18h
                if (!isInTimeRange(heureDebut, heureFin, LocalTime.of(9, 0), LocalTime.of(13, 0)) &&
                        !isInTimeRange(heureDebut, heureFin, LocalTime.of(14, 0), LocalTime.of(18, 0))) {
                    throw new IllegalArgumentException(
                            "Les créneaux du " + jour + " doivent être entre 9h-13h ou 14h-18h"
                    );
                }
                break;
            case SATURDAY:
                // Samedi : 9h-14h
                if (!isInTimeRange(heureDebut, heureFin, LocalTime.of(9, 0), LocalTime.of(14, 0))) {
                    throw new IllegalArgumentException(
                            "Les créneaux du samedi doivent être entre 9h-14h"
                    );
                }
                break;
            case SUNDAY:
                throw new IllegalArgumentException("Aucun créneau n'est autorisé le dimanche");
        }
    }

    /**
     * Vérifie si un créneau est dans une plage horaire donnée
     */
    private boolean isInTimeRange(LocalTime debut, LocalTime fin, LocalTime rangeDebut, LocalTime rangeFin) {
        return !debut.isBefore(rangeDebut) && !fin.isAfter(rangeFin);
    }

    /**
     * Vérifie s'il y a un conflit horaire avec un autre créneau
     */
    private boolean hasTimeConflict(CreneauDisponibilite creneau) {
        List<CreneauDisponibilite> existingCreneaux = creneauDisponibiliteRepository
                .findByJourSemaineAndActifTrue(creneau.getJourSemaine());

        return existingCreneaux.stream()
                .anyMatch(existing -> timesOverlap(
                        creneau.getHeureDebut(), creneau.getHeureFin(),
                        existing.getHeureDebut(), existing.getHeureFin()
                ));
    }

    /**
     * Vérifie s'il y a un conflit horaire en excluant un ID spécifique
     */
    private boolean hasTimeConflictExcluding(CreneauDisponibilite creneau, Long excludeId) {
        List<CreneauDisponibilite> existingCreneaux = creneauDisponibiliteRepository
                .findByJourSemaineAndActifTrue(creneau.getJourSemaine());

        return existingCreneaux.stream()
                .filter(existing -> !existing.getId().equals(excludeId))
                .anyMatch(existing -> timesOverlap(
                        creneau.getHeureDebut(), creneau.getHeureFin(),
                        existing.getHeureDebut(), existing.getHeureFin()
                ));
    }

    /**
     * Vérifie si deux plages horaires se chevauchent
     */
    private boolean timesOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    // ========== MÉTHODES SUPPLÉMENTAIRES UTILES ==========

    /**
     * Trouve un créneau de disponibilité de manière optionnelle
     */
    public Optional<CreneauDisponibilite> findByIdOptional(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return creneauDisponibiliteRepository.findById(id);
    }

    /**
     * Compte le nombre total de créneaux de disponibilité
     */
    public long countAll() {
        return creneauDisponibiliteRepository.count();
    }

    /**
     * Compte le nombre de créneaux actifs
     */
    public long countActive() {
        return creneauDisponibiliteRepository.countByActifTrue();
    }

    /**
     * Trouve tous les créneaux pour un jour donné (actifs et inactifs)
     */
    public List<CreneauDisponibilite> findAllByJourSemaine(DayOfWeek jourSemaine) {
        if (jourSemaine == null) {
            throw new IllegalArgumentException("Le jour de la semaine ne peut pas être null");
        }
        return creneauDisponibiliteRepository.findByJourSemaine(jourSemaine);
    }

    /**
     * Active ou désactive tous les créneaux d'un jour donné
     */
    @Transactional
    public int toggleDayAvailability(DayOfWeek jourSemaine, boolean actif) {
        if (jourSemaine == null) {
            throw new IllegalArgumentException("Le jour de la semaine ne peut pas être null");
        }

        List<CreneauDisponibilite> creneauxDuJour = findAllByJourSemaine(jourSemaine);

        for (CreneauDisponibilite creneau : creneauxDuJour) {
            creneau.setActif(actif);
        }

        creneauDisponibiliteRepository.saveAll(creneauxDuJour);

        // Synchroniser les créneaux concrets
        if (creneauGenerationService != null) {
            creneauGenerationService.synchronizeCreneauxWithDisponibilite();
        }

        return creneauxDuJour.size();
    }
}