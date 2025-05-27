package com.example.security.service.impl;

import com.example.security.dao.CreneauDisponibiliteRepository;
import com.example.security.dao.CreneauRepository;
import com.example.security.entity.Creneau;
import com.example.security.entity.CreneauDisponibilite;
import com.example.security.service.facade.CreneauGenerationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CreneauGenerationServiceImpl implements CreneauGenerationService {

    private final CreneauDisponibiliteRepository creneauDisponibiliteRepository;
    private final CreneauRepository creneauRepository;

    public CreneauGenerationServiceImpl(CreneauDisponibiliteRepository creneauDisponibiliteRepository,
                                        CreneauRepository creneauRepository) {
        this.creneauDisponibiliteRepository = creneauDisponibiliteRepository;
        this.creneauRepository = creneauRepository;
    }

    @Override
    public void initializeDefaultCreneauxDisponibilite() {
        creneauDisponibiliteRepository.deleteAll();
        createDefaultCreneauxDisponibilite();
    }

    private void createDefaultCreneauxDisponibilite() {
        List<CreneauDisponibilite> defaultCreneaux = new ArrayList<>();

        // LUNDI à VENDREDI : 9h-13h et 14h-18h
        for (DayOfWeek day : List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)) {
            defaultCreneaux.addAll(createCreneauxForTimeRange(day, 9, 13));
            defaultCreneaux.addAll(createCreneauxForTimeRange(day, 14, 18));
        }

        // SAMEDI : 9h-14h
        defaultCreneaux.addAll(createCreneauxForTimeRange(DayOfWeek.SATURDAY, 9, 14));

        creneauDisponibiliteRepository.saveAll(defaultCreneaux);

        System.out.println("=== INITIALISATION DES CRÉNEAUX PAR DÉFAUT ===");
        System.out.println("Total créneaux créés : " + defaultCreneaux.size());

        for (DayOfWeek day : DayOfWeek.values()) {
            long count = defaultCreneaux.stream()
                    .filter(c -> c.getJourSemaine().equals(day))
                    .count();
            if (count > 0) {
                System.out.println(day + " : " + count + " créneaux");
                defaultCreneaux.stream()
                        .filter(c -> c.getJourSemaine().equals(day))
                        .forEach(c -> System.out.println("  - " + c.getHeureDebut() + " à " + c.getHeureFin()));
            }
        }
        System.out.println("===============================================");
    }

    private List<CreneauDisponibilite> createCreneauxForTimeRange(DayOfWeek day, int heureDebut, int heureFin) {
        List<CreneauDisponibilite> creneaux = new ArrayList<>();
        System.out.println("Création créneaux pour " + day + " de " + heureDebut + "h à " + heureFin + "h");

        for (int hour = heureDebut; hour < heureFin; hour++) {
            CreneauDisponibilite creneau = new CreneauDisponibilite();
            creneau.setJourSemaine(day);
            creneau.setHeureDebut(LocalTime.of(hour, 0));
            creneau.setHeureFin(LocalTime.of(hour + 1, 0));
            creneau.setCapaciteMax(4);
            creneau.setActif(true);
            creneau.setCreeParAdmin(false);
            creneaux.add(creneau);

            System.out.println("  Créneau créé : " + hour + "h00 - " + (hour + 1) + "h00");
        }

        return creneaux;
    }

    @Override
    public void regenerateAllFutureCreneaux() {
        LocalDate aujourdhui = LocalDate.now();

        // 1. Supprimer les créneaux futurs non réservés
        List<Creneau> creneauxASupprimer = creneauRepository.findByDateCreneauAfterAndCapaciteRestanteGreaterThan(
                aujourdhui, 0);
        creneauxASupprimer = creneauxASupprimer.stream()
                .filter(c -> c.getCapaciteRestante().equals(c.getCreneauDisponibilite().getCapaciteMax()))
                .toList();
        creneauRepository.deleteAll(creneauxASupprimer);

        System.out.println("Suppression de " + creneauxASupprimer.size() + " créneaux futurs non réservés");

        // 2. ✅ AJOUT : Générer les créneaux pour les prochaines semaines
        generateCreneauxForPeriod(aujourdhui, aujourdhui.plusWeeks(4)); // 4 semaines à l'avance
    }

    /**
     * ✅ NOUVELLE MÉTHODE : Génère les créneaux pour une période donnée
     */
    public void generateCreneauxForPeriod(LocalDate dateDebut, LocalDate dateFin) {
        List<CreneauDisponibilite> creneauxDisponibles = creneauDisponibiliteRepository.findByActifTrue();
        List<Creneau> nouveauxCreneaux = new ArrayList<>();

        System.out.println("=== GÉNÉRATION DES CRÉNEAUX ===");
        System.out.println("Période : " + dateDebut + " à " + dateFin);
        System.out.println("Créneaux disponibles trouvés : " + creneauxDisponibles.size());

        for (LocalDate date = dateDebut; !date.isAfter(dateFin); date = date.plusDays(1)) {
            DayOfWeek jourSemaine = date.getDayOfWeek();

            // Trouver les créneaux disponibles pour ce jour
            List<CreneauDisponibilite> creneauxDuJour = creneauxDisponibles.stream()
                    .filter(cd -> cd.getJourSemaine().equals(jourSemaine))
                    .toList();

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
                    nouveauCreneau.setCreneauDisponibilite(creneauDispo); // ✅ LIEN CRUCIAL

                    nouveauxCreneaux.add(nouveauCreneau);
                }
            }
        }

        if (!nouveauxCreneaux.isEmpty()) {
            creneauRepository.saveAll(nouveauxCreneaux);
            System.out.println("Créneaux générés : " + nouveauxCreneaux.size());
        } else {
            System.out.println("Aucun nouveau créneau à générer");
        }
        System.out.println("================================");
    }

    /**
     * ✅ NOUVELLE MÉTHODE : Génère les créneaux pour les prochaines semaines
     */
    public void generateFutureCreneaux() {
        LocalDate aujourdhui = LocalDate.now();
        LocalDate dateLimite = aujourdhui.plusWeeks(6); // Générer 6 semaines à l'avance
        generateCreneauxForPeriod(aujourdhui, dateLimite);
    }

    @Override
    public void cleanupPastCreneaux() {
        LocalDate dateLimit = LocalDate.now().minusDays(7);
        List<Creneau> creneauxASupprimer = creneauRepository.findByDateCreneauBefore(dateLimit);

        if (!creneauxASupprimer.isEmpty()) {
            creneauRepository.deleteAll(creneauxASupprimer);
            System.out.println("Nettoyage effectué : " + creneauxASupprimer.size() + " créneaux passés supprimés");
        }
    }
}