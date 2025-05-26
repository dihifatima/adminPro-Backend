package com.example.security.ws.facade;

import com.example.security.entity.CreneauDisponibilite;
import com.example.security.service.facade.CreneauDisponibiliteService;
import com.example.security.service.facade.CreneauGenerationService;
import com.example.security.ws.converter.CreneauDisponibiliteConverter;
import com.example.security.ws.dto.CreneauDisponibiliteDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/creneaux-gestion")

public class AdminCreneauxController {

    @Autowired
    private CreneauDisponibiliteService creneauDisponibiliteService;

    @Autowired
    private CreneauGenerationService creneauGenerationService;

    @Autowired
    private CreneauDisponibiliteConverter creneauDisponibiliteConverter;

    // ===== INITIALISATION DU SYSTÈME =====

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

    @PostMapping("/reset-to-default")
    public ResponseEntity<?> resetToDefaultCreneaux() {
        try {
            // Supprimer tous les créneaux de disponibilité existants
            List<CreneauDisponibilite> allCreneaux = creneauDisponibiliteService.findAll();
            for (CreneauDisponibilite creneau : allCreneaux) {
                creneauDisponibiliteService.deleteById(creneau.getId());
            }

            // Réinitialiser avec les créneaux par défaut
            creneauGenerationService.initializeDefaultCreneauxDisponibilite();
            creneauGenerationService.regenerateAllFutureCreneaux();

            return ResponseEntity.ok("Système réinitialisé aux créneaux par défaut");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la réinitialisation : " + e.getMessage());
        }
    }

    // ===== GESTION PERSONNALISÉE DES CRÉNEAUX =====

    @PostMapping("/create-custom")
    public ResponseEntity<?> createCustomCreneau(@Valid @RequestBody CreneauDisponibiliteDto dto) {
        try {
            CreneauDisponibilite entity = creneauDisponibiliteConverter.map(dto);
            entity.setCreeParAdmin(true); // Marquer comme créé par l'admin
            entity.setActif(true);

            CreneauDisponibilite saved = creneauDisponibiliteService.save(entity);

            // Régénérer les créneaux futurs pour appliquer ce nouveau créneau
            creneauGenerationService.updateCreneauxAfterDisponibiliteChange(saved.getId());

            CreneauDisponibiliteDto savedDto = creneauDisponibiliteConverter.map(saved);
            return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur lors de la création : " + e.getMessage());
        }
    }

    @PutMapping("/update-custom/{id}")
    public ResponseEntity<?> updateCustomCreneau(@PathVariable Long id, @Valid @RequestBody CreneauDisponibiliteDto dto) {
        try {
            CreneauDisponibilite entity = creneauDisponibiliteConverter.map(dto);
            entity.setId(id);
            entity.setCreeParAdmin(true);

            CreneauDisponibilite updated = creneauDisponibiliteService.update(entity);

            // Mettre à jour les créneaux futurs
            creneauGenerationService.updateCreneauxAfterDisponibiliteChange(id);

            CreneauDisponibiliteDto updatedDto = creneauDisponibiliteConverter.map(updated);
            return ResponseEntity.ok(updatedDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-custom/{id}")
    public ResponseEntity<?> deleteCustomCreneau(@PathVariable Long id) {
        try {
            CreneauDisponibilite creneau = creneauDisponibiliteService.findById(id);

            if (!creneau.getCreeParAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Impossible de supprimer un créneau système par défaut");
            }

            creneauDisponibiliteService.deleteById(id);

            // Synchroniser les créneaux
            creneauGenerationService.synchronizeCreneauxWithDisponibilite();

            return ResponseEntity.ok("Créneau personnalisé supprimé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    // ===== GÉNÉRATION ET SYNCHRONISATION =====

    @PostMapping("/generate-period")
    public ResponseEntity<?> generateCreneauxForPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        try {
            creneauGenerationService.generateCreneauxForPeriod(dateDebut, dateFin);
            return ResponseEntity.ok("Créneaux générés pour la période " + dateDebut + " à " + dateFin);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la génération : " + e.getMessage());
        }
    }

    @PostMapping("/generate-next-30-days")
    public ResponseEntity<?> generateNext30Days() {
        try {
            creneauGenerationService.generateCreneauxForNext30Days();
            return ResponseEntity.ok("Créneaux générés pour les 30 prochains jours");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la génération : " + e.getMessage());
        }
    }

    @PostMapping("/regenerate-all")
    public ResponseEntity<?> regenerateAllFutureCreneaux() {
        try {
            creneauGenerationService.regenerateAllFutureCreneaux();
            return ResponseEntity.ok("Tous les créneaux futurs ont été régénérés");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la régénération : " + e.getMessage());
        }
    }

    @PostMapping("/synchronize")
    public ResponseEntity<?> synchronizeCreneaux() {
        try {
            creneauGenerationService.synchronizeCreneauxWithDisponibilite();
            return ResponseEntity.ok("Synchronisation terminée");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la synchronisation : " + e.getMessage());
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

    // ===== VUES ET STATISTIQUES =====

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

    @GetMapping("/preview-generation/{days}")
    public ResponseEntity<?> previewCreneauxGeneration(@PathVariable int days) {
        try {
            LocalDate dateDebut = LocalDate.now();
            LocalDate dateFin = dateDebut.plusDays(days);

            List<CreneauDisponibilite> creneauxDisponibles = creneauDisponibiliteService.findAllActive();

            Map<String, Object> preview = new HashMap<>();
            preview.put("periode", dateDebut + " à " + dateFin);
            preview.put("nombre_jours", days);
            preview.put("creneaux_disponibilite_actifs", creneauxDisponibles.size());

            // Calculer le nombre approximatif de créneaux qui seront générés
            long totalCreneauxEstime = 0;
            for (LocalDate date = dateDebut; !date.isAfter(dateFin); date = date.plusDays(1)) {
                final LocalDate currentDate = date;
                long creneauxPourCeJour = creneauxDisponibles.stream()
                        .filter(cd -> cd.getJourSemaine().equals(currentDate.getDayOfWeek()))
                        .count();
                totalCreneauxEstime += creneauxPourCeJour;
            }

            preview.put("total_creneaux_estimes", totalCreneauxEstime);
            preview.put("creneaux_disponibilite", creneauDisponibiliteConverter.mapListEntities(creneauxDisponibles));

            return ResponseEntity.ok(preview);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la prévisualisation : " + e.getMessage());
        }
    }

    // ===== ACTIVATION/DÉSACTIVATION EN MASSE =====

    @PutMapping("/toggle-all-default/{actif}")
    public ResponseEntity<?> toggleAllDefaultCreneaux(@PathVariable boolean actif) {
        try {
            List<CreneauDisponibilite> defaultCreneaux = creneauDisponibiliteService.findAll().stream()
                    .filter(c -> !c.getCreeParAdmin())
                    .toList();

            for (CreneauDisponibilite creneau : defaultCreneaux) {
                if (actif) {
                    creneauDisponibiliteService.activate(creneau.getId());
                } else {
                    creneauDisponibiliteService.deactivate(creneau.getId());
                }
            }

            creneauGenerationService.synchronizeCreneauxWithDisponibilite();

            String action = actif ? "activés" : "désactivés";
            return ResponseEntity.ok("Tous les créneaux par défaut ont été " + action);
        } catch (Exception e) {
            Object preview = null;
            return ResponseEntity.ok(preview);
        }
    }

// ===== LISTE DES CRÉNEAUX =====

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

    @GetMapping("/actifs")
    public ResponseEntity<?> findAllActifs() {
        try {
            List<CreneauDisponibilite> creneaux = creneauDisponibiliteService.findAllActive();
            List<CreneauDisponibiliteDto> dtos = creneauDisponibiliteConverter.mapListEntities(creneaux);
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération des créneaux actifs : " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            CreneauDisponibilite creneau = creneauDisponibiliteService.findById(id);
            if (creneau == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Créneau non trouvé avec l'identifiant : " + id);
            }
            CreneauDisponibiliteDto dto = creneauDisponibiliteConverter.map(creneau);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération du créneau : " + e.getMessage());
        }
    }
}
