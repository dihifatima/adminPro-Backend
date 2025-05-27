package com.example.security.ws.facade;
import com.example.security.entity.Creneau;
import com.example.security.service.facade.CreneauService;
import com.example.security.ws.converter.CreneauConverter;
import com.example.security.ws.dto.CreneauDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/creneaux")
public class CreneauController {

    private final  CreneauService creneauService;
    private  final CreneauConverter creneauConverter;

    public CreneauController(CreneauService creneauService, CreneauConverter creneauConverter) {
        this.creneauService = creneauService;
        this.creneauConverter = creneauConverter;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody CreneauDto dto) {
        try {
            Creneau entity = creneauConverter.map(dto);
            Creneau saved = creneauService.save(entity);
            CreneauDto savedDto = creneauConverter.map(saved);
            return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
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
            Creneau found = creneauService.findById(id);
            CreneauDto dto = creneauConverter.map(found);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Créneau non trouvé : " + e.getMessage());
        }
    }
    @GetMapping("/all")
    public ResponseEntity<List<CreneauDto>> getAll() {
        List<Creneau> entities = creneauService.findAll();
        List<CreneauDto> dtos = creneauConverter.mapListEntities(entities);
        return ResponseEntity.ok(dtos);
    }
    @GetMapping("/available-check/{id}")
    public ResponseEntity<Boolean> isAvailable(@PathVariable Long id) {
        try {
            boolean available = creneauService.isCreneauAvailable(id);
            return ResponseEntity.ok(available);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }

    @PutMapping("/reserver/{id}")
    public ResponseEntity<?> reserver(@PathVariable Long id) {
        try {
            Creneau reserved = creneauService.reserverCreneau(id);
            CreneauDto dto = creneauConverter.map(reserved);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur lors de la réservation : " + e.getMessage());
        }
    }

    @PutMapping("/liberer/{id}")
    public ResponseEntity<?> liberer(@PathVariable Long id) {
        try {
            Creneau liberated = creneauService.libererCreneau(id);
            CreneauDto dto = creneauConverter.map(liberated);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur lors de la libération : " + e.getMessage());
        }
    }
}