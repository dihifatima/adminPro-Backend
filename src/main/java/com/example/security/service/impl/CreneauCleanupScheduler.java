package com.example.security.service.impl;

import com.example.security.service.facade.CreneauService;
import com.example.security.service.facade.GenerationCreneauxParDefautService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class CreneauCleanupScheduler {

    private final CreneauService creneauService;
    private final GenerationCreneauxParDefautService generationService;

    public CreneauCleanupScheduler(CreneauService creneauService,
                                   GenerationCreneauxParDefautService generationService) {
        this.creneauService = creneauService;
        this.generationService = generationService;
    }

    /**
     * 🔥 NOUVELLE TÂCHE : Nettoyage + Génération automatique tous les jours à 1h du matin
     */
    @Scheduled(cron = "0 0 1 * * *")
    public void dailyCreneauxMaintenance() {
        System.out.println("=== MAINTENANCE QUOTIDIENNE DES CRÉNEAUX (1h du matin) ===");

        // 1. Nettoyer les créneaux passés
        creneauService.cleanupPassedCreneaux();

        // 2. Générer les nouveaux créneaux pour les semaines à venir
        generationService.generateFutureCreneaux();

        System.out.println("=== FIN DE LA MAINTENANCE QUOTIDIENNE ===");
    }

    /**
     * 🔥 NOUVELLE TÂCHE : Génération proactive à 6h du matin (heures de pointe)
     */
    @Scheduled(cron = "0 0 6 * * *")
    public void morningCreneauxGeneration() {
        System.out.println("=== GÉNÉRATION MATINALE DES CRÉNEAUX (6h du matin) ===");
        generationService.generateFutureCreneaux();
    }

    /**
     * Maintenance complète au démarrage de l'application
     */
    @EventListener(ApplicationReadyEvent.class)
    public void maintenanceOnStartup() {
        System.out.println("=== MAINTENANCE COMPLÈTE AU DÉMARRAGE ===");

        try {
            // 1. Nettoyer les créneaux passés
            creneauService.cleanupPassedCreneaux();

            // 2. Générer les créneaux futurs
            generationService.generateFutureCreneaux();

            System.out.println("✅ Maintenance au démarrage terminée avec succès");
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la maintenance au démarrage : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Maintenance immédiate lors de l'initialisation du bean
     */
    @PostConstruct
    public void immediateMaintenanceCreneaux() {
        System.out.println("=== MAINTENANCE IMMÉDIATE DES CRÉNEAUX ===");
        try {
            // Nettoyer d'abord
            creneauService.cleanupPassedCreneaux();

            // Puis générer
            generationService.generateFutureCreneaux();

            System.out.println("✅ Maintenance immédiate terminée");
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la maintenance immédiate : " + e.getMessage());
        }
    }

    /**
     * Nettoyage hebdomadaire des créneaux très anciens (plus de 30 jours)
     * Tous les dimanches à 2h du matin
     */
    @Scheduled(cron = "0 0 2 * * SUN")
    public void weeklyOldCreneauxCleanup() {
        System.out.println("=== NETTOYAGE HEBDOMADAIRE DES CRÉNEAUX ANCIENS ===");
        creneauService.cleanupOldCreneaux(30); // Supprimer les créneaux de plus de 30 jours
    }

    /**
     * 🔥 NOUVELLE TÂCHE : Maintenance pendant les heures d'ouverture (9h-17h)
     * Toutes les 2 heures pour s'assurer que les créneaux sont toujours disponibles
     */
    @Scheduled(cron = "0 0 9,11,13,15,17 * * MON-SAT")
    public void businessHoursCreneauxCheck() {
        System.out.println("=== VÉRIFICATION CRÉNEAUX PENDANT HEURES D'OUVERTURE ===");

        // Nettoyer les créneaux passés en temps réel
        creneauService.cleanupPassedCreneaux();

        // Générer de nouveaux créneaux si nécessaire
        generationService.generateFutureCreneaux();
    }

    /**
     * 🔥 TÂCHE SPÉCIALE : Régénération complète le dimanche soir à 23h
     * Pour préparer la semaine suivante
     */
    @Scheduled(cron = "0 0 23 * * SUN")
    public void weeklyCompleteRegeneration() {
        System.out.println("=== RÉGÉNÉRATION COMPLÈTE HEBDOMADAIRE (Dimanche 23h) ===");

        try {
            // Régénérer tous les créneaux futurs
            generationService.regenerateAllFutureCreneaux();
            System.out.println("✅ Régénération complète terminée");
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la régénération complète : " + e.getMessage());
        }
    }

    /**
     * 🚨 MÉTHODE DE DÉBOGAGE : Force la génération (à activer manuellement si besoin)
     * Utile pour les tests ou dépannage
     */
    // @Scheduled(fixedRate = 60000) // Toutes les minutes (décommenter pour debug)
    public void debugCreneauxGeneration() {
        System.out.println("🐛 DEBUG - Génération forcée des créneaux");
        generationService.generateFutureCreneaux();
    }
}