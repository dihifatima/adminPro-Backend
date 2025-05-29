package com.example.security.ws.facade;
import com.example.security.entity.Creneau;
import com.example.security.entity.CreneauDisponibilite;
import com.example.security.service.facade.ParametrageCreneauDisponibiliteService;
import com.example.security.service.facade.GenerationCreneauxParDefautService;
import com.example.security.service.facade.CreneauService;
import com.example.security.ws.converter.CreneauConverter;
import com.example.security.ws.converter.CreneauDisponibiliteConverter;
import com.example.security.ws.dto.CreneauDisponibiliteDto;
import com.example.security.ws.dto.CreneauDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/creneaux-gestion")
public class GenerationCreneauxParDefautController {
    private final ParametrageCreneauDisponibiliteService creneauDisponibiliteService;
    private final GenerationCreneauxParDefautService generationCreneauxParDefautService;
    private final CreneauDisponibiliteConverter creneauDisponibiliteConverter;
    private final CreneauConverter creneauConverter;
    private final CreneauService creneauService;
    public GenerationCreneauxParDefautController(ParametrageCreneauDisponibiliteService creneauDisponibiliteService,
                                                 GenerationCreneauxParDefautService generationCreneauxParDefautService,
                                                 CreneauDisponibiliteConverter creneauDisponibiliteConverter, CreneauConverter creneauConverter, CreneauService creneauService) {
        this.creneauDisponibiliteService = creneauDisponibiliteService;
        this.generationCreneauxParDefautService = generationCreneauxParDefautService;
        this.creneauDisponibiliteConverter = creneauDisponibiliteConverter;
        this.creneauConverter = creneauConverter;
        this.creneauService = creneauService;
    }

    @PostMapping("/generate-future")
    public ResponseEntity<?> generateFutureCreneaux() {
        try {
            generationCreneauxParDefautService.generateFutureCreneaux();
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

            generationCreneauxParDefautService.initializeDefaultCreneauxDisponibilite();
            generationCreneauxParDefautService.regenerateAllFutureCreneaux();
            generationCreneauxParDefautService.generateFutureCreneaux();

            return ResponseEntity.ok("Système réinitialisé aux créneaux par défaut");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la réinitialisation : " + e.getMessage());
        }
    }

    @PostMapping("/cleanup-past")
    public ResponseEntity<?> cleanupPastCreneaux() {
        try {
            generationCreneauxParDefautService.cleanupPastCreneaux();
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
    @GetMapping("/disponibles")
    public ResponseEntity<List<CreneauDto>> getCreneauxDisponibles() {
        try {
            // Récupérer tous les créneaux actifs depuis la base de données
            List<Creneau> creneauxActifs = creneauService.findAllActiveCreneaux();

            // Convertir en DTO pour le frontend
            List<CreneauDto> creneauxDto = creneauConverter.mapListEntities(creneauxActifs);

            return ResponseEntity.ok(creneauxDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

       /* @PostMapping("/initialize-default")
    public ResponseEntity<?> initializeDefaultCreneaux() {
        try {
            creneauGenerationService.initializeDefaultCreneauxDisponibilite();
            return ResponseEntity.ok("Créneaux par défaut initialisés avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'initialisation : " + e.getMessage());
        }
    }*/

}