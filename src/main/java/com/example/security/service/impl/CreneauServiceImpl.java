package com.example.security.service.impl;

import com.example.security.dao.CreneauRepository;
import com.example.security.dao.CreneauDisponibiliteRepository;
import com.example.security.entity.Creneau;
import com.example.security.entity.CreneauDisponibilite;
import com.example.security.service.facade.CreneauDisponibiliteService;
import com.example.security.service.facade.CreneauService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CreneauServiceImpl implements CreneauService {

    @Autowired
    private final CreneauRepository creneauRepository;

    @Autowired
    private final CreneauDisponibiliteService creneauDisponibiliteService;

    // ✅ AJOUT : Repository pour trouver le CreneauDisponibilite correspondant
    @Autowired
    private final CreneauDisponibiliteRepository creneauDisponibiliteRepository;

    public CreneauServiceImpl(CreneauRepository creneauRepository,
                              CreneauDisponibiliteService creneauDisponibiliteService,
                              CreneauDisponibiliteRepository creneauDisponibiliteRepository) {
        this.creneauRepository = creneauRepository;
        this.creneauDisponibiliteService = creneauDisponibiliteService;
        this.creneauDisponibiliteRepository = creneauDisponibiliteRepository;
    }

    @Override
    public Creneau save(Creneau creneau) {
        if (creneau == null) {
            throw new RuntimeException("Creneau ne peut pas être null");
        }

        // ✅ CORRECTION PRINCIPALE : Lier automatiquement le CreneauDisponibilite
        if (creneau.getCreneauDisponibilite() == null) {
            CreneauDisponibilite creneauDispo = findMatchingCreneauDisponibilite(creneau);
            if (creneauDispo == null) {
                throw new RuntimeException(
                        "Aucun créneau de disponibilité trouvé pour " +
                                creneau.getDateCreneau().getDayOfWeek() +
                                " de " + creneau.getHeureDebut() + " à " + creneau.getHeureFin()
                );
            }
            creneau.setCreneauDisponibilite(creneauDispo);
            // Définir la capacité restante basée sur la capacité max
            if (creneau.getCapaciteRestante() == null) {
                creneau.setCapaciteRestante(creneauDispo.getCapaciteMax());
            }
        }

        validateCreneau(creneau);
        return creneauRepository.save(creneau);
    }

    /**
     * ✅ NOUVELLE MÉTHODE : Trouve le CreneauDisponibilite correspondant
     */
    private CreneauDisponibilite findMatchingCreneauDisponibilite(Creneau creneau) {
        if (creneau.getDateCreneau() == null || creneau.getHeureDebut() == null || creneau.getHeureFin() == null) {
            return null;
        }

        // Trouver par jour de la semaine, heure début et heure fin
        List<CreneauDisponibilite> candidats = creneauDisponibiliteRepository
                .findByJourSemaineAndActifTrue(creneau.getDateCreneau().getDayOfWeek());

        return candidats.stream()
                .filter(cd -> cd.getHeureDebut().equals(creneau.getHeureDebut()) &&
                        cd.getHeureFin().equals(creneau.getHeureFin()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Creneau findById(Long id) {
        return creneauRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Creneau non trouvé avec l'ID : " + id));
    }

    @Override
    public List<Creneau> findAll() {
        return creneauRepository.findAll();
    }

    @Override
    public boolean isCreneauAvailable(Long creneauId) {
        Creneau creneau = findById(creneauId);
        return creneau.getActif() && creneau.getCapaciteRestante() > 0;
    }

    @Override
    public Creneau reserverCreneau(Long creneauId) {
        Creneau creneau = findById(creneauId);

        if (!creneau.getActif()) {
            throw new RuntimeException("Ce créneau n'est pas actif");
        }

        if (creneau.getCapaciteRestante() <= 0) {
            throw new RuntimeException("Ce créneau n'a plus de places disponibles");
        }

        creneau.setCapaciteRestante(creneau.getCapaciteRestante() - 1);
        return creneauRepository.save(creneau);
    }

    @Override
    public Creneau libererCreneau(Long creneauId) {
        Creneau creneau = findById(creneauId);

        // Vérifier qu'on ne dépasse pas la capacité maximale
        int capaciteMax = creneau.getCreneauDisponibilite().getCapaciteMax();
        if (creneau.getCapaciteRestante() >= capaciteMax) {
            throw new RuntimeException("La capacité maximale est déjà atteinte");
        }

        creneau.setCapaciteRestante(creneau.getCapaciteRestante() + 1);
        return creneauRepository.save(creneau);
    }

    private void validateCreneau(Creneau creneau) {
        if (creneau.getDateCreneau() == null) {
            throw new RuntimeException("La date du créneau est obligatoire");
        }

        if (creneau.getHeureDebut() == null || creneau.getHeureFin() == null) {
            throw new RuntimeException("Les heures de début et fin sont obligatoires");
        }

        if (creneau.getHeureDebut().isAfter(creneau.getHeureFin()) ||
                creneau.getHeureDebut().equals(creneau.getHeureFin())) {
            throw new RuntimeException("L'heure de début doit être antérieure à l'heure de fin");
        }

        if (creneau.getCapaciteRestante() == null || creneau.getCapaciteRestante() < 0) {
            throw new RuntimeException("La capacité restante ne peut pas être négative");
        }

        // ✅ VALIDATION RENFORCÉE : CreneauDisponibilite est maintenant obligatoire
        if (creneau.getCreneauDisponibilite() == null) {
            throw new RuntimeException("Le créneau doit être lié à un créneau de disponibilité");
        }

        if (creneau.getCapaciteRestante() > creneau.getCreneauDisponibilite().getCapaciteMax()) {
            throw new RuntimeException("La capacité restante ne peut pas dépasser la capacité maximale");
        }

        // Vérifier que la date n'est pas dans le passé (sauf pour les mises à jour)
        if (creneau.getId() == null && creneau.getDateCreneau().isBefore(LocalDate.now())) {
            throw new RuntimeException("Impossible de créer un créneau dans le passé");
        }

        // ✅ VALIDATION : Vérifier que le créneau correspond aux horaires autorisés
        if (!isCreneauInValidTimeRange(creneau)) {
            throw new RuntimeException(
                    "Le créneau ne correspond pas aux horaires autorisés pour " +
                            creneau.getDateCreneau().getDayOfWeek()
            );
        }
    }

    /**
     * ✅ NOUVELLE MÉTHODE : Vérifie si le créneau est dans les horaires autorisés
     */
    private boolean isCreneauInValidTimeRange(Creneau creneau) {
        CreneauDisponibilite creneauDispo = creneau.getCreneauDisponibilite();
        return creneauDispo.getJourSemaine().equals(creneau.getDateCreneau().getDayOfWeek()) &&
                creneauDispo.getHeureDebut().equals(creneau.getHeureDebut()) &&
                creneauDispo.getHeureFin().equals(creneau.getHeureFin()) &&
                creneauDispo.getActif();
    }
}