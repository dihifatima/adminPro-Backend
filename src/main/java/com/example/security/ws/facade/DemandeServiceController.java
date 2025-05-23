package com.example.security.ws.facade;

import com.example.security.entity.DemandeService;
import com.example.security.service.facade.DemandeServiceService;
import com.example.security.ws.converter.DemandeServiceConverter;
import com.example.security.ws.dto.DemandeServiceDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/demandesService")
public class DemandeServiceController {
    @Autowired
    private DemandeServiceService demandeServiceService;

    @Autowired
    private DemandeServiceConverter demandeServiceConverter;

    @PostMapping("/create")
    public ResponseEntity<?> createDemande(@Valid @RequestBody DemandeServiceDto demandeServiceDto) {
        try {
            DemandeService demandeService = demandeServiceConverter.map(demandeServiceDto);
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
    @PutMapping("/statut")
    public ResponseEntity<?> updateStatut(@RequestParam String ref, @RequestParam String statut) {
        try {
            DemandeService updated = demandeServiceService.updateStatut(ref, statut);
            return ResponseEntity.ok(demandeServiceConverter.map(updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }

    // ✅ Chercher par référence
    @GetMapping("/ref/{ref}")
    public ResponseEntity<?> getByRef(@PathVariable String ref) {
        try {
            DemandeService found = demandeServiceService.findByRef(ref);
            return ResponseEntity.ok(demandeServiceConverter.map(found));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demande non trouvée : " + e.getMessage());
        }
    }

    // ✅ Supprimer par référence
    @DeleteMapping("/delete/{ref}")
    public ResponseEntity<?> deleteByRef(@PathVariable String ref) {
        try {
            int deleted = demandeServiceService.deleteByRef(ref);
            return ResponseEntity.ok("Demande supprimée avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erreur : " + e.getMessage());
        }
    }

    // ✅ Afficher toutes les demandes
    @GetMapping("/all")
    public ResponseEntity<List<DemandeServiceDto>> getAll() {
        List<DemandeService> demandes = demandeServiceService.findAll();
        return ResponseEntity.ok(demandes.stream().map(demandeServiceConverter::map).toList());
    }

    // ✅ Chercher par nom utilisateur
    @GetMapping("/user")
    public ResponseEntity<List<DemandeServiceDto>> getByUserNom(@RequestParam String nom) {
        List<DemandeService> demandes = demandeServiceService.findByUserFullName(nom);
        return ResponseEntity.ok(demandes.stream().map(demandeServiceConverter::map).toList());
    }

    // ✅ Chercher par nom du service offert
    @GetMapping("/service")
    public ResponseEntity<List<DemandeServiceDto>> getByServiceNom(@RequestParam String nom) {
        List<DemandeService> demandes = demandeServiceService.findByServiceOffertNom(nom);
        return ResponseEntity.ok(demandes.stream().map(demandeServiceConverter::map).toList());
    }
    // ✅ Récupérer toutes les dates de rendez-vous réservées
    @GetMapping("/dates-reservees")
    public ResponseEntity<List<LocalDateTime>> getReservedDates() {
        List<LocalDateTime> dates = demandeServiceService.findAllReservedDates();
        return ResponseEntity.ok(dates);
    }




}