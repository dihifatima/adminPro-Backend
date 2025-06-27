package com.example.security.service.impl;

import com.example.security.dao.CreneauxRepository;
import com.example.security.dao.CreneauDisponibiliteRepository;
import com.example.security.entity.Creneau;
import com.example.security.service.facade.CreneauService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CreneauServiceImpl implements CreneauService {

    private final CreneauxRepository creneauRepository;
    private final CreneauDisponibiliteRepository creneauDisponibiliteRepository;

    public CreneauServiceImpl(CreneauxRepository creneauRepository, CreneauDisponibiliteRepository creneauDisponibiliteRepository) {
        this.creneauRepository = creneauRepository;
        this.creneauDisponibiliteRepository = creneauDisponibiliteRepository;
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

        // Vérifier si le créneau est actif et a de la capacité restante
        if (!creneau.getActif() || creneau.getCapaciteRestante() <= 0) {
            return false;
        }

        // Vérifier si le créneau n'est pas dans le passé
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        if (creneau.getDateCreneau().isBefore(today)) {
            return false; // Date complètement passée
        } else if (creneau.getDateCreneau().equals(today)) {
            // Si c'est aujourd'hui, vérifier l'heure de début
            LocalDateTime creneauDateTime = LocalDateTime.of(
                    creneau.getDateCreneau(),
                    creneau.getHeureDebut()
            );
            if (creneauDateTime.isBefore(now)) {
                return false; // Heure passée aujourd'hui
            }
        }

        // Vérifier que le CreneauDisponibilite parent est aussi actif
        return creneau.getCreneauDisponibilite() != null &&
                creneau.getCreneauDisponibilite().getActif();
    }

    @Override
    public Creneau updateCreneauStatus(Long creneauId, Boolean actif) {
        Creneau creneau = findById(creneauId);
        creneau.setActif(actif);
        return creneauRepository.save(creneau);
    }

    @Override
    public List<Creneau> findAvailableCreneauxForBooking() {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        return creneauRepository.findByActifTrueAndCapaciteRestanteGreaterThan(0)
                .stream()
                .filter(creneau -> {
                    // Vérifier que le créneau est dans le futur (pas aujourd'hui passé)
                    if (creneau.getDateCreneau().isAfter(today)) {
                        return true; // Date future : OK
                    } else if (creneau.getDateCreneau().equals(today)) {
                        // Si c'est aujourd'hui, vérifier l'heure de début
                        LocalDateTime creneauDateTime = LocalDateTime.of(
                                creneau.getDateCreneau(),
                                creneau.getHeureDebut()
                        );
                        return creneauDateTime.isAfter(now);
                    } else {
                        return false; // Date passée : KO
                    }
                })
                .filter(creneau -> {
                    // Vérifier que le CreneauDisponibilite parent est aussi actif
                    return creneau.getCreneauDisponibilite() != null &&
                            creneau.getCreneauDisponibilite().getActif();
                })
                .collect(Collectors.toList());
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

        // Vérifier si le créneau n'est pas dans le passé
        if (!isCreneauAvailable(creneauId)) {
            throw new RuntimeException("Ce créneau n'est plus disponible (passé ou inactif)");
        }

        creneau.setCapaciteRestante(creneau.getCapaciteRestante() - 1);
        return creneauRepository.save(creneau);
    }

    @Override
    public void cleanupPassedCreneaux() {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        List<Creneau> creneauxToCleanup = creneauRepository.findAll()
                .stream()
                .filter(creneau -> {
                    if (creneau.getDateCreneau().isBefore(today)) {
                        return true; // Date complètement passée
                    } else if (creneau.getDateCreneau().equals(today)) {
                        // Si c'est aujourd'hui, vérifier l'heure de fin
                        LocalDateTime creneauEndDateTime = LocalDateTime.of(
                                creneau.getDateCreneau(),
                                creneau.getHeureFin()
                        );
                        return creneauEndDateTime.isBefore(now);
                    }
                    return false;
                })
                .filter(creneau -> creneau.getActif()) // Ne traiter que les créneaux encore actifs
                .toList();

        if (!creneauxToCleanup.isEmpty()) {
            // OPTION 1: Désactiver au lieu de supprimer (recommandé pour conserver l'historique)
            creneauxToCleanup.forEach(creneau -> {
                creneau.setActif(false);
                System.out.println("Créneau désactivé : " + creneau.getDateCreneau() +
                        " " + creneau.getHeureDebut() + "-" + creneau.getHeureFin());
            });
            creneauRepository.saveAll(creneauxToCleanup);

            // OPTION 2: Supprimer complètement (décommenter si préféré)
            // creneauRepository.deleteAll(creneauxToCleanup);

            System.out.println(" Nettoyage effectué : " + creneauxToCleanup.size() +
                    " créneaux passés désactivés");
        } else {
            System.out.println("Aucun créneau passé à nettoyer");
        }
    }

    @Override
    public void cleanupOldCreneaux(int daysOld) {
        LocalDate cutoffDate = LocalDate.now().minusDays(daysOld);

        List<Creneau> oldCreneaux = creneauRepository.findByDateCreneauBefore(cutoffDate);

        if (!oldCreneaux.isEmpty()) {
            // Supprimer les créneaux vraiment anciens
            creneauRepository.deleteAll(oldCreneaux);
            System.out.println("🗑️ Suppression de " + oldCreneaux.size() +
                    " créneaux de plus de " + daysOld + " jours");
        }
    }
}