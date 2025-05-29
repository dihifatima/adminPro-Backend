package com.example.security.service.impl;
import com.example.security.dao.CreneauDisponibiliteRepository;
import com.example.security.entity.CreneauDisponibilite;
import com.example.security.service.facade.ParametrageCreneauDisponibiliteService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ParametrageCreneauxDisponibiliteServiceImpl implements ParametrageCreneauDisponibiliteService {

    @Autowired
    private final CreneauDisponibiliteRepository creneauDisponibiliteRepository;
    public ParametrageCreneauxDisponibiliteServiceImpl(CreneauDisponibiliteRepository creneauDisponibiliteRepository) {
        this.creneauDisponibiliteRepository = creneauDisponibiliteRepository;
    }


    @Override
    public CreneauDisponibilite update(CreneauDisponibilite creneauDisponibilite) {
        if (creneauDisponibilite == null || creneauDisponibilite.getId() == null) {
            throw new IllegalArgumentException("CreneauDisponibilite ou son ID ne peut pas être null");
        }
        Optional<CreneauDisponibilite> existing = creneauDisponibiliteRepository.findById(creneauDisponibilite.getId());
        validateCreneauDisponibilite(creneauDisponibilite);
        if (hasTimeConflictExcluding(creneauDisponibilite, creneauDisponibilite.getId())) {
            throw new IllegalArgumentException(
                    "Un autre créneau existe déjà pour ce jour et ces horaires : " +
                            creneauDisponibilite.getJourSemaine() + " " +
                            creneauDisponibilite.getHeureDebut() + "-" + creneauDisponibilite.getHeureFin()
            );
        }
        CreneauDisponibilite updated = creneauDisponibiliteRepository.save(creneauDisponibilite);
        return updated;
    }



    @Override
    public List<CreneauDisponibilite> findAll() {
        return creneauDisponibiliteRepository.findAll();
    }


    @Override
    public List<CreneauDisponibilite> findByJourSemaine(DayOfWeek jour) {
        return creneauDisponibiliteRepository.findByJourSemaine(jour);
    }


    @Override
    @Transactional
    public int deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        Optional<CreneauDisponibilite> optional = creneauDisponibiliteRepository.findById(id);
        if (optional.isPresent()) {
            creneauDisponibiliteRepository.deleteById(id);
            return 1;
        } else {
            throw new RuntimeException("Créneau avec l'ID " + id + " introuvable.");
        }
    }


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

        validateBusinessHours(creneauDisponibilite);
    }

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

    private boolean isInTimeRange(LocalTime debut, LocalTime fin, LocalTime rangeDebut, LocalTime rangeFin) {
        return !debut.isBefore(rangeDebut) && !fin.isAfter(rangeFin);
    }

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

    private boolean timesOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }


}