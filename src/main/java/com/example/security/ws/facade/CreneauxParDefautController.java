package com.example.security.ws.facade;

import com.example.security.entity.CreneauDisponibilite;
import com.example.security.service.facade.CreneauDisponibiliteService;
import com.example.security.service.facade.CreneauGenerationService;
import com.example.security.ws.converter.CreneauDisponibiliteConverter;
import com.example.security.ws.dto.CreneauDisponibiliteDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/creneaux-gestion")
public class CreneauxParDefautController {

    private final CreneauDisponibiliteService creneauDisponibiliteService;
    private final CreneauGenerationService creneauGenerationService;
    private final CreneauDisponibiliteConverter creneauDisponibiliteConverter;

    public CreneauxParDefautController(CreneauDisponibiliteService creneauDisponibiliteService,
                                       CreneauGenerationService creneauGenerationService,
                                       CreneauDisponibiliteConverter creneauDisponibiliteConverter) {
        this.creneauDisponibiliteService = creneauDisponibiliteService;
        this.creneauGenerationService = creneauGenerationService;
        this.creneauDisponibiliteConverter = creneauDisponibiliteConverter;
    }

    @PostMapping("/initialize-default")
    public ResponseEntity<?> initializeDefaultCreneaux() {
        try {
            creneauGenerationService.initializeDefaultCreneauxDisponibilite();
            return ResponseEntity.ok("Créneaux par défaut initialisés avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'initialisation : " + e.getMessage());
        }
    }

    // ✅ NOUVEAU : Endpoint pour générer les créneaux futurs
    @PostMapping("/generate-future")
    public ResponseEntity<?> generateFutureCreneaux() {
        try {
            creneauGenerationService.generateFutureCreneaux();
            return ResponseEntity.ok("Créneaux futurs générés avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la génération : " + e.getMessage());
        }
    }

    @PostMapping("/reset-to-default")
    public ResponseEntity<?> resetToDefaultCreneaux() {
        try {
            List<CreneauDisponibilite> allCreneaux = creneauDisponibiliteService.findAll();
            for (CreneauDisponibilite creneau : allCreneaux) {
                creneauDisponibiliteService.deleteById(creneau.getId());
            }

            creneauGenerationService.initializeDefaultCreneauxDisponibilite();
            creneauGenerationService.regenerateAllFutureCreneaux();

            // ✅ AJOUT : Générer les créneaux après reset
            creneauGenerationService.generateFutureCreneaux();

            return ResponseEntity.ok("Système réinitialisé aux créneaux par défaut");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la réinitialisation : " + e.getMessage());
        }
    }

    @PostMapping("/cleanup-past")
    public ResponseEntity<?> cleanupPastCreneaux() {
        try {
            creneauGenerationService.cleanupPastCreneaux();
            return ResponseEntity.ok("Créneaux passés nettoyés");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du nettoyage : " + e.getMessage());
        }
    }

    @GetMapping("/view-configuration")
    public ResponseEntity<Map<String, Object>> viewCurrentConfiguration() {
        try {
            List<CreneauDisponibilite> allCreneaux = creneauDisponibiliteService.findAll();
            List<CreneauDisponibilite> defaultCreneaux = allCreneaux.stream()
                    .filter(c -> !c.getCreeParAdmin())
                    .toList();
            List<CreneauDisponibilite> customCreneaux = allCreneaux.stream()
                    .filter(CreneauDisponibilite::getCreeParAdmin)
                    .toList();

            Map<String, Object> config = new HashMap<>();
            config.put("total_creneaux", allCreneaux.size());
            config.put("default_creneaux", defaultCreneaux.size());
            config.put("custom_creneaux", customCreneaux.size());
            config.put("creneaux_actifs", allCreneaux.stream().filter(CreneauDisponibilite::getActif).count());

            config.put("default_creneaux_list", creneauDisponibiliteConverter.mapListEntities(defaultCreneaux));
            config.put("custom_creneaux_list", creneauDisponibiliteConverter.mapListEntities(customCreneaux));

            return ResponseEntity.ok(config);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        try {
            List<CreneauDisponibilite> creneaux = creneauDisponibiliteService.findAll();
            List<CreneauDisponibiliteDto> dtos = creneauDisponibiliteConverter.mapListEntities(creneaux);
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération des créneaux : " + e.getMessage());
        }
    }
}