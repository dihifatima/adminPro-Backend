package com.example.security.ws.facade;
import com.example.security.entity.DemandeService;
import com.example.security.service.facade.CreneauService;
import com.example.security.service.facade.DemandeServiceService;
import com.example.security.ws.converter.DemandeServiceConverter;
import com.example.security.ws.dto.DemandeServiceDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/demandesService")
public class DemandeServiceController {

    private  final DemandeServiceService demandeServiceService;

    private  final CreneauService creneauService;

    private  final DemandeServiceConverter demandeServiceConverter;

    public DemandeServiceController(DemandeServiceService demandeServiceService, CreneauService creneauService, DemandeServiceConverter demandeServiceConverter) {
        this.demandeServiceService = demandeServiceService;
        this.creneauService = creneauService;
        this.demandeServiceConverter = demandeServiceConverter;
    }


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






}