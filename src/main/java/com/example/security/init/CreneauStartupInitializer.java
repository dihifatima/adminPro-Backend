package com.example.security.init;

import com.example.security.service.facade.GenerationCreneauxParDefautService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CreneauStartupInitializer {

    private final GenerationCreneauxParDefautService generationService;

    @Value("${creneau.auto.init:true}")
    private boolean shouldInit;

    public CreneauStartupInitializer(GenerationCreneauxParDefautService generationService) {
        this.generationService = generationService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        if (shouldInit) {
            System.out.println("=== Initialisation automatique des créneaux activée ===");

            generationService.initializeDefaultCreneauxDisponibilite();
            generationService.generateFutureCreneaux();

            System.out.println("=== Créneaux générés avec succès ===");
        } else {
            System.out.println("=== Initialisation automatique des créneaux désactivée ===");
        }
    }
}
