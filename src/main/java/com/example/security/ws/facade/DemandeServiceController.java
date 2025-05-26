package com.example.security.ws.facade;

import com.example.security.entity.DemandeService;
import com.example.security.service.facade.CreneauService;
import com.example.security.service.facade.DemandeServiceService;
import com.example.security.ws.converter.DemandeServiceConverter;
import com.example.security.ws.dto.DemandeServiceDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/demandesService")
public class DemandeServiceController {

    @Autowired
    private DemandeServiceService demandeServiceService;
    @Autowired
    private CreneauService creneauService;
    @Autowired
    private DemandeServiceConverter demandeServiceConverter;

    // ===== OPÉRATIONS CRUD DE BASE =====

    // Dans DemandeServiceController.java - Modification de la méthode create

    @PostMapping("/create")
    public ResponseEntity<?> createDemande(@Valid @RequestBody DemandeServiceDto demandeServiceDto) {
        try {
            DemandeService demandeService = demandeServiceConverter.map(demandeServiceDto);

            // Si un créneau est spécifié dans la demande
            if (demandeServiceDto.getCreneau() != null) {
                // Vérifier que le créneau existe et est disponible
                if (!creneauService.isCreneauAvailable(demandeServiceDto.getCreneau())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Le créneau sélectionné n'est pas disponible");
                }
                // Le créneau sera associé dans le service
            }

            DemandeService savedDemandeService = demandeServiceService.save(demandeService);
            DemandeServiceDto savedDto = demandeServiceConverter.map(savedDemandeService);
            return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Erreur : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur inattendue lors de la création de la demande : " + e.getMessage());
        }
    }

    // Nouvelle méthode pour créer une demande avec créneau directement
    @PostMapping("/create-avec-creneau")
    public ResponseEntity<?> createDemandeAvecCreneau(
            @Valid @RequestBody DemandeServiceDto demandeServiceDto,
            @RequestParam Long creneau) {
        try {
            DemandeService demandeService = demandeServiceConverter.map(demandeServiceDto);
            DemandeService savedDemandeService = demandeServiceService.saveAvecCreneau(demandeService, creneau);
            DemandeServiceDto savedDto = demandeServiceConverter.map(savedDemandeService);
            return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Erreur : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur inattendue : " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateDemande(@Valid @RequestBody DemandeServiceDto demandeServiceDto) {
        try {
            DemandeService demandeService = demandeServiceConverter.map(demandeServiceDto);
            DemandeService updatedDemandeService = demandeServiceService.update(demandeService);
            DemandeServiceDto updatedDto = demandeServiceConverter.map(updatedDemandeService);
            return ResponseEntity.ok(updatedDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur inattendue : " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            DemandeService found = demandeServiceService.findById(id);
            DemandeServiceDto dto = demandeServiceConverter.map(found);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Demande non trouvée : " + e.getMessage());
        }
    }

    @GetMapping("/ref/{ref}")
    public ResponseEntity<?> getByRef(@PathVariable String ref) {
        try {
            DemandeService found = demandeServiceService.findByRef(ref);
            return ResponseEntity.ok(demandeServiceConverter.map(found));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Demande non trouvée : " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<DemandeServiceDto>> getAll() {
        List<DemandeService> demandes = demandeServiceService.findAll();
        return ResponseEntity.ok(demandes.stream().map(demandeServiceConverter::map).toList());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            int deleted = demandeServiceService.deleteById(id);
            return ResponseEntity.ok("Demande supprimée avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur : " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/ref/{ref}")
    public ResponseEntity<?> deleteByRef(@PathVariable String ref) {
        try {
            int deleted = demandeServiceService.deleteByRef(ref);
            return ResponseEntity.ok("Demande supprimée avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur : " + e.getMessage());
        }
    }

    // ===== GESTION DES STATUTS =====

    @PutMapping("/statut")
    public ResponseEntity<?> updateStatut(@RequestParam String ref, @RequestParam String statut) {
        try {
            DemandeService updated = demandeServiceService.updateStatut(ref, statut);
            return ResponseEntity.ok(demandeServiceConverter.map(updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur : " + e.getMessage());
        }
    }

    @PutMapping("/accepter/{ref}")
    public ResponseEntity<?> accepterDemande(@PathVariable String ref, @RequestParam Long creneauId) {
        try {
            DemandeService accepted = demandeServiceService.accepterDemande(ref, creneauId);
            return ResponseEntity.ok(demandeServiceConverter.map(accepted));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur lors de l'acceptation : " + e.getMessage());
        }
    }

    @PutMapping("/refuser/{ref}")
    public ResponseEntity<?> refuserDemande(@PathVariable String ref, @RequestParam(required = false) String motif) {
        try {
            DemandeService refused = demandeServiceService.refuserDemande(ref, motif);
            return ResponseEntity.ok(demandeServiceConverter.map(refused));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur lors du refus : " + e.getMessage());
        }
    }

    @PutMapping("/annuler/{ref}")
    public ResponseEntity<?> annulerDemande(@PathVariable String ref) {
        try {
            DemandeService cancelled = demandeServiceService.annulerDemande(ref);
            return ResponseEntity.ok(demandeServiceConverter.map(cancelled));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur lors de l'annulation : " + e.getMessage());
        }
    }

    // ===== RECHERCHES SPÉCIFIQUES =====

    @GetMapping("/user")
    public ResponseEntity<List<DemandeServiceDto>> getByUserNom(@RequestParam String nom) {
        List<DemandeService> demandes = demandeServiceService.findByUserFullName(nom);
        return ResponseEntity.ok(demandes.stream().map(demandeServiceConverter::map).toList());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DemandeServiceDto>> getByUserId(@PathVariable Long userId) {
        List<DemandeService> demandes = demandeServiceService.findByUser(userId);
        return ResponseEntity.ok(demandes.stream().map(demandeServiceConverter::map).toList());
    }

    @GetMapping("/service")
    public ResponseEntity<List<DemandeServiceDto>> getByServiceNom(@RequestParam String nom) {
        List<DemandeService> demandes = demandeServiceService.findByServiceOffertNom(nom);
        return ResponseEntity.ok(demandes.stream().map(demandeServiceConverter::map).toList());
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<DemandeServiceDto>> getByStatut(@PathVariable String statut) {
        List<DemandeService> demandes = demandeServiceService.findByStatut(statut);
        return ResponseEntity.ok(demandes.stream().map(demandeServiceConverter::map).toList());
    }

    @GetMapping("/en-attente")
    public ResponseEntity<List<DemandeServiceDto>> getDemandesEnAttente() {
        List<DemandeService> demandes = demandeServiceService.findDemandesEnAttente();
        return ResponseEntity.ok(demandes.stream().map(demandeServiceConverter::map).toList());
    }

    @GetMapping("/acceptees")
    public ResponseEntity<List<DemandeServiceDto>> getDemandesAcceptees() {
        List<DemandeService> demandes = demandeServiceService.findDemandesAcceptees();
        return ResponseEntity.ok(demandes.stream().map(demandeServiceConverter::map).toList());
    }

    @GetMapping("/refusees")
    public ResponseEntity<List<DemandeServiceDto>> getDemandesRefusees() {
        List<DemandeService> demandes = demandeServiceService.findDemandesRefusees();
        return ResponseEntity.ok(demandes.stream().map(demandeServiceConverter::map).toList());
    }

    // ===== GESTION DES DATES =====

    @GetMapping("/dates-reservees")
    public ResponseEntity<List<LocalDateTime>> getReservedDates() {
        List<LocalDateTime> dates = demandeServiceService.findAllReservedDates();
        return ResponseEntity.ok(dates);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<DemandeServiceDto>> getByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<DemandeService> demandes = demandeServiceService.findByDateRendezvous(date);
        return ResponseEntity.ok(demandes.stream().map(demandeServiceConverter::map).toList());
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<DemandeServiceDto>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        List<DemandeService> demandes = demandeServiceService.findByDateRange(dateDebut, dateFin);
        return ResponseEntity.ok(demandes.stream().map(demandeServiceConverter::map).toList());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<DemandeServiceDto>> getRecentDemandes(@RequestParam(defaultValue = "7") int jours) {
        List<DemandeService> demandes = demandeServiceService.findRecentDemandes(jours);
        return ResponseEntity.ok(demandes.stream().map(demandeServiceConverter::map).toList());
    }

    @GetMapping("/date-disponible")
    public ResponseEntity<Boolean> isDateDisponible(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateRendezvous) {
        boolean disponible = demandeServiceService.isDateDisponible(dateRendezvous);
        return ResponseEntity.ok(disponible);
    }

    // ===== STATISTIQUES =====

    @GetMapping("/stats/statut")
    public ResponseEntity<Map<String, Object>> getStatsByStatut() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("en_attente", demandeServiceService.countDemandesByStatut("EN_ATTENTE"));
        stats.put("acceptees", demandeServiceService.countDemandesByStatut("ACCEPTEE"));
        stats.put("refusees", demandeServiceService.countDemandesByStatut("REFUSEE"));
        stats.put("annulees", demandeServiceService.countDemandesByStatut("ANNULEE"));
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/user/{userId}")
    public ResponseEntity<Long> getStatsByUser(@PathVariable Long userId) {
        long count = demandeServiceService.countDemandesByUser(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/service/{serviceId}")
    public ResponseEntity<Long> getStatsByService(@PathVariable Long serviceId) {
        long count = demandeServiceService.countDemandesByService(serviceId);
        return ResponseEntity.ok(count);
    }

    // ===== VALIDATION =====

    @GetMapping("/can-create")
    public ResponseEntity<Boolean> canUserCreateDemande(@RequestParam String userEmail, @RequestParam String serviceName) {
        boolean canCreate = demandeServiceService.canUserCreateDemande(userEmail, serviceName);
        return ResponseEntity.ok(canCreate);
    }

    @GetMapping("/has-demande")
    public ResponseEntity<Boolean> hasDemandeForUserAndService(@RequestParam String userEmail, @RequestParam String serviceName) {
        boolean hasDemande = demandeServiceService.hasDemandeForUserAndService(userEmail, serviceName);
        return ResponseEntity.ok(hasDemande);
    }
}