package com.example.security.controller;

import com.example.security.entity.Creneau;
import com.example.security.entity.CreneauDisponibilite;
import com.example.security.service.facade.CapaciteManagementService;
import com.example.security.service.impl.CapaciteManagementServiceImpl.CapaciteApplicationStrategy;
import com.example.security.service.impl.CapaciteManagementServiceImpl.CapaciteChangeReport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/capacite")
@CrossOrigin(origins = "http://localhost:4200")
public class CapaciteManagementController {

    private final CapaciteManagementService capaciteManagementService;

    public CapaciteManagementController(CapaciteManagementService capaciteManagementService) {
        this.capaciteManagementService = capaciteManagementService;
    }

    /**
     * Modifie la capacité d'un créneau spécifique
     */
    @PutMapping("/creneau/{creneauId}/capacite")
    public ResponseEntity<?> updateCapaciteCreneau(
            @PathVariable Long creneauId,
            @RequestBody Map<String, Integer> request) {

        try {
            Integer nouvelleCapacite = request.get("nouvelleCapacite");
            if (nouvelleCapacite == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "La nouvelle capacité est requise"));
            }

            Creneau creneau = capaciteManagementService.updateCapaciteSpecifiqueCreneau(
                    creneauId, nouvelleCapacite);

            return ResponseEntity.ok(Map.of(
                    "message", "Capacité mise à jour avec succès",
                    "creneau", creneau
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Modifie la capacité par défaut avec options d'application
     */
    @PutMapping("/creneau-disponibilite/{id}/capacite")
    public ResponseEntity<?> updateCapaciteParDefaut(
            @PathVariable Long id,
            @RequestBody CapaciteUpdateRequest request) {

        try {
            CreneauDisponibilite updated = capaciteManagementService.updateCapaciteAvecChoixApplication(
                    id,
                    request.getNouvelleCapacite(),
                    request.getStrategy(),
                    request.getDateDebut()
            );

            return ResponseEntity.ok(Map.of(
                    "message", "Capacité par défaut mise à jour",
                    "creneauDisponibilite", updated,
                    "strategie", request.getStrategy()
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Analyse l'impact d'un changement de capacité avant de l'appliquer
     */
    @PostMapping("/creneau-disponibilite/{id}/analyser-impact")
    public ResponseEntity<?> analyserImpact(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {

        try {
            Integer nouvelleCapacite = (Integer) request.get("nouvelleCapacite");
            LocalDate dateDebut = request.get("dateDebut") != null
                    ? LocalDate.parse((String) request.get("dateDebut"))
                    : null;

            CapaciteChangeReport report = capaciteManagementService.analyserImpactChangementCapacite(
                    id, nouvelleCapacite, dateDebut);

            return ResponseEntity.ok(Map.of(
                    "hasProblems", report.hasProblems(),
                    "creneauxProblematiques", report.getCreneauxProblematiques(),
                    "creneauxOk", report.getCreneauxOk(),
                    "totalCreneaux", report.getCreneauxOk().size() + report.getCreneauxProblematiques().size()
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Remet un créneau à sa capacité par défaut
     */
    @PutMapping("/creneau/{creneauId}/reset-capacite")
    public ResponseEntity<?> resetCapaciteParDefaut(@PathVariable Long creneauId) {
        try {
            Creneau creneau = capaciteManagementService.resetCapaciteParDefaut(creneauId);

            return ResponseEntity.ok(Map.of(
                    "message", "Capacité remise par défaut",
                    "creneau", creneau
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // DTO pour les requêtes
    public static class CapaciteUpdateRequest {
        private Integer nouvelleCapacite;
        private CapaciteApplicationStrategy strategy;
        private LocalDate dateDebut;

        // Constructeurs, getters, setters
        public CapaciteUpdateRequest() {}

        public Integer getNouvelleCapacite() { return nouvelleCapacite; }
        public void setNouvelleCapacite(Integer nouvelleCapacite) { this.nouvelleCapacite = nouvelleCapacite; }

        public CapaciteApplicationStrategy getStrategy() { return strategy; }
        public void setStrategy(CapaciteApplicationStrategy strategy) { this.strategy = strategy; }

        public LocalDate getDateDebut() { return dateDebut; }
        public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    }
}