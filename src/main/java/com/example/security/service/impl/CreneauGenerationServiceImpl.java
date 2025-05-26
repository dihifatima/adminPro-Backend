package com.example.security.service.impl;

import com.example.security.dao.CreneauDisponibiliteRepository;
import com.example.security.dao.CreneauRepository;
import com.example.security.entity.Creneau;
import com.example.security.entity.CreneauDisponibilite;
import com.example.security.service.facade.CreneauGenerationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CreneauGenerationServiceImpl implements CreneauGenerationService {

    @Autowired
    private CreneauDisponibiliteRepository creneauDisponibiliteRepository;

    @Autowired
    private CreneauRepository creneauRepository;

    /**
     * Initialise les créneaux de disponibilité par défaut si aucun n'existe
     */
    @Override
    public void initializeDefaultCreneauxDisponibilite() {
        // Supprimer tous les créneaux existants pour réinitialiser complètement
        creneauDisponibiliteRepository.deleteAll();

        // Créer les nouveaux créneaux par défaut
        createDefaultCreneauxDisponibilite();
    }

    /**
     * Crée les créneaux de disponibilité par défaut selon les horaires d'ouverture :
     * - Lundi à Vendredi : 9h-13h et 14h-18h
     * - Samedi : 9h-14h
     * - Dimanche : fermé
     */
    private void createDefaultCreneauxDisponibilite() {
        List<CreneauDisponibilite> defaultCreneaux = new ArrayList<>();

        // LUNDI à VENDREDI : 9h-13h et 14h-18h
        for (DayOfWeek day : List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)) {

            // Créer les créneaux du matin (9h-13h) - 4 créneaux : 9h, 10h, 11h, 12h
            defaultCreneaux.addAll(createCreneauxForTimeRange(day, 9, 13));

            // Créer les créneaux de l'après-midi (14h-18h) - 4 créneaux : 14h, 15h, 16h, 17h
            defaultCreneaux.addAll(createCreneauxForTimeRange(day, 14, 18));
        }

        // SAMEDI : 9h-14h - 5 créneaux : 9h, 10h, 11h, 12h, 13h
        defaultCreneaux.addAll(createCreneauxForTimeRange(DayOfWeek.SATURDAY, 9, 14));

        // Sauvegarder tous les créneaux par défaut
        creneauDisponibiliteRepository.saveAll(defaultCreneaux);

        System.out.println("=== INITIALISATION DES CRÉNEAUX PAR DÉFAUT ===");
        System.out.println("Total créneaux créés : " + defaultCreneaux.size());

        // Afficher le détail par jour
        for (DayOfWeek day : DayOfWeek.values()) {
            long count = defaultCreneaux.stream()
                    .filter(c -> c.getJourSemaine().equals(day))
                    .count();
            if (count > 0) {
                System.out.println(day + " : " + count + " créneaux");
                // Afficher les heures pour debug
                defaultCreneaux.stream()
                        .filter(c -> c.getJourSemaine().equals(day))
                        .forEach(c -> System.out.println("  - " + c.getHeureDebut() + " à " + c.getHeureFin()));
            }
        }
        System.out.println("===============================================");
    }

    /**
     * Méthode utilitaire pour créer des créneaux d'une heure dans une plage horaire donnée
     */
    private List<CreneauDisponibilite> createCreneauxForTimeRange(DayOfWeek day, int heureDebut, int heureFin) {
        List<CreneauDisponibilite> creneaux = new ArrayList<>();

        System.out.println("Création créneaux pour " + day + " de " + heureDebut + "h à " + heureFin + "h");

        for (int hour = heureDebut; hour < heureFin; hour++) {
            CreneauDisponibilite creneau = new CreneauDisponibilite();
            creneau.setJourSemaine(day);
            creneau.setHeureDebut(LocalTime.of(hour, 0));
            creneau.setHeureFin(LocalTime.of(hour + 1, 0));
            creneau.setCapaciteMax(4); // 4 clients max par créneau
            creneau.setDureeMinutes(60); // Créneaux d'1 heure
            creneau.setActif(true);
            creneau.setCreeParAdmin(false); // Créé par défaut du système
            creneaux.add(creneau);

            System.out.println("  Créneau créé : " + hour + "h00 - " + (hour + 1) + "h00");
        }

        return creneaux;
    }

    /**
     * Génère les créneaux concrets pour une période donnée
     */
    @Override
    public void generateCreneauxForPeriod(LocalDate dateDebut, LocalDate dateFin) {
        List<CreneauDisponibilite> creneauxDisponibles = creneauDisponibiliteRepository.findByActifTrue();
        int creneauxCrees = 0;

        System.out.println("=== GÉNÉRATION CRÉNEAUX POUR PÉRIODE ===");
        System.out.println("Période : " + dateDebut + " à " + dateFin);
        System.out.println("Créneaux disponibilité actifs : " + creneauxDisponibles.size());

        for (LocalDate date = dateDebut; !date.isAfter(dateFin); date = date.plusDays(1)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            // Skip dimanche (pas de créneaux le dimanche)
            if (dayOfWeek == DayOfWeek.SUNDAY) {
                continue;
            }

            // Filtrer les créneaux disponibles pour ce jour
            List<CreneauDisponibilite> creneauxDuJour = creneauxDisponibles.stream()
                    .filter(cd -> cd.getJourSemaine().equals(dayOfWeek))
                    .toList();

            System.out.println(date + " (" + dayOfWeek + ") : " + creneauxDuJour.size() + " créneaux disponibles");

            for (CreneauDisponibilite creneauDispo : creneauxDuJour) {
                // Vérifier si un créneau existe déjà pour cette date/heure
                boolean creneauExiste = creneauRepository.existsByDateCreneauAndHeureDebutAndHeureFin(
                        date, creneauDispo.getHeureDebut(), creneauDispo.getHeureFin());

                if (!creneauExiste) {
                    Creneau nouveauCreneau = new Creneau();
                    nouveauCreneau.setDateCreneau(date);
                    nouveauCreneau.setHeureDebut(creneauDispo.getHeureDebut());
                    nouveauCreneau.setHeureFin(creneauDispo.getHeureFin());
                    nouveauCreneau.setCapaciteRestante(creneauDispo.getCapaciteMax());
                    nouveauCreneau.setActif(true);
                    nouveauCreneau.setCreneauDisponibilite(creneauDispo);

                    creneauRepository.save(nouveauCreneau);
                    creneauxCrees++;
                }
            }
        }

        System.out.println("Génération terminée : " + creneauxCrees + " nouveaux créneaux créés");
        System.out.println("========================================");
    }

    /**
     * Génère automatiquement les créneaux pour les 30 prochains jours
     */
    @Override
    public void generateCreneauxForNext30Days() {
        LocalDate aujourdhui = LocalDate.now();
        LocalDate dans30Jours = aujourdhui.plusDays(30);
        generateCreneauxForPeriod(aujourdhui, dans30Jours);
    }

    /**
     * Régénère tous les créneaux futurs basés sur les créneaux de disponibilité actuels
     */
    @Override
    public void regenerateAllFutureCreneaux() {
        LocalDate aujourdhui = LocalDate.now();

        // Supprimer les créneaux futurs non réservés (capacité restante = capacité max)
        List<Creneau> creneauxASupprimer = creneauRepository.findByDateCreneauAfterAndCapaciteRestanteGreaterThan(
                aujourdhui, 0);

        // Ne supprimer que les créneaux complètement libres
        creneauxASupprimer = creneauxASupprimer.stream()
                .filter(c -> c.getCapaciteRestante().equals(c.getCreneauDisponibilite().getCapaciteMax()))
                .toList();

        creneauRepository.deleteAll(creneauxASupprimer);

        System.out.println("Suppression de " + creneauxASupprimer.size() + " créneaux futurs non réservés");

        // Régénérer pour les 60 prochains jours
        LocalDate dans60Jours = aujourdhui.plusDays(60);
        generateCreneauxForPeriod(aujourdhui, dans60Jours);
    }

    /**
     * Met à jour les créneaux existants quand un CreneauDisponibilite est modifié
     */
    @Override
    public void updateCreneauxAfterDisponibiliteChange(Long creneauDisponibiliteId) {
        CreneauDisponibilite creneauDispo = creneauDisponibiliteRepository.findById(creneauDisponibiliteId)
                .orElseThrow(() -> new RuntimeException("CreneauDisponibilite non trouvé avec l'ID : " + creneauDisponibiliteId));

        LocalDate aujourdhui = LocalDate.now();

        // Trouver tous les créneaux futurs liés à ce créneau de disponibilité
        List<Creneau> creneauxExistants = creneauRepository.findByCreneauDisponibiliteIdAndDateCreneauAfter(
                creneauDisponibiliteId, aujourdhui);

        int creneauxMisAJour = 0;

        for (Creneau creneau : creneauxExistants) {
            // Ne mettre à jour que les créneaux complètement libres (pas de réservations)
            if (creneau.getCapaciteRestante().equals(creneau.getCreneauDisponibilite().getCapaciteMax())) {
                creneau.setActif(creneauDispo.getActif());
                creneau.setHeureDebut(creneauDispo.getHeureDebut());
                creneau.setHeureFin(creneauDispo.getHeureFin());
                creneau.setCapaciteRestante(creneauDispo.getCapaciteMax());
                creneauRepository.save(creneau);
                creneauxMisAJour++;
            }
        }

        System.out.println("Mise à jour de " + creneauxMisAJour + " créneaux après modification du créneau de disponibilité " + creneauDisponibiliteId);
    }

    /**
     * Vérifie et active/désactive les créneaux selon la disponibilité
     */
    @Override
    public void synchronizeCreneauxWithDisponibilite() {
        List<CreneauDisponibilite> creneauxDisponibles = creneauDisponibiliteRepository.findAll();
        LocalDate aujourdhui = LocalDate.now();
        int creneauxSynchronises = 0;

        for (CreneauDisponibilite creneauDispo : creneauxDisponibles) {
            List<Creneau> creneauxLies = creneauRepository.findByCreneauDisponibiliteIdAndDateCreneauAfter(
                    creneauDispo.getId(), aujourdhui);

            for (Creneau creneau : creneauxLies) {
                // Synchroniser le statut actif
                if (!creneau.getActif().equals(creneauDispo.getActif())) {
                    creneau.setActif(creneauDispo.getActif());
                    creneauRepository.save(creneau);
                    creneauxSynchronises++;
                }
            }
        }

        System.out.println("Synchronisation terminée : " + creneauxSynchronises + " créneaux synchronisés");
    }

    /**
     * Nettoie les créneaux passés (garde un historique d'une semaine)
     */
    @Override
    public void cleanupPastCreneaux() {
        LocalDate dateLimit = LocalDate.now().minusDays(7); // Garder 7 jours d'historique
        List<Creneau> creneauxASupprimer = creneauRepository.findByDateCreneauBefore(dateLimit);

        if (!creneauxASupprimer.isEmpty()) {
            creneauRepository.deleteAll(creneauxASupprimer);
            System.out.println("Nettoyage effectué : " + creneauxASupprimer.size() + " créneaux passés supprimés");
        }
    }

    /**
     * Méthode utilitaire pour afficher un résumé des créneaux de disponibilité
     */
    public void displayCreneauxDisponibiliteSummary() {
        List<CreneauDisponibilite> tous = creneauDisponibiliteRepository.findAll();
        List<CreneauDisponibilite> actifs = creneauDisponibiliteRepository.findByActifTrue();

        System.out.println("=== RÉSUMÉ DES CRÉNEAUX DE DISPONIBILITÉ ===");
        System.out.println("Total : " + tous.size() + " créneaux");
        System.out.println("Actifs : " + actifs.size() + " créneaux");

        for (DayOfWeek day : DayOfWeek.values()) {
            long count = actifs.stream().filter(c -> c.getJourSemaine() == day).count();
            if (count > 0) {
                System.out.println(day + " : " + count + " créneaux actifs");
            }
        }
    }
}