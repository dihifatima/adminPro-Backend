package com.example.security.ws.facade;

import com.example.security.entity.CreneauDisponibilite;
import com.example.security.service.facade.CreneauDisponibiliteService;
import com.example.security.ws.converter.CreneauDisponibiliteConverter;
import com.example.security.ws.dto.CreneauDisponibiliteDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/creneaux-disponibilite")
public class CreneauDisponibiliteController {

    @Autowired
    private CreneauDisponibiliteService creneauDisponibiliteService;

    @Autowired
    private CreneauDisponibiliteConverter creneauDisponibiliteConverter;

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody CreneauDisponibiliteDto dto) {
        try {
            CreneauDisponibilite entity = creneauDisponibiliteConverter.map(dto);
            entity.setCreeParAdmin(true); // Marqué comme créé par admin
            CreneauDisponibilite saved = creneauDisponibiliteService.save(entity);
            CreneauDisponibiliteDto savedDto = creneauDisponibiliteConverter.map(saved);
            return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur inattendue : " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody CreneauDisponibiliteDto dto) {
        try {
            CreneauDisponibilite entity = creneauDisponibiliteConverter.map(dto);
            CreneauDisponibilite updated = creneauDisponibiliteService.update(entity);
            CreneauDisponibiliteDto updatedDto = creneauDisponibiliteConverter.map(updated);
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
            CreneauDisponibilite found = creneauDisponibiliteService.findById(id);
            CreneauDisponibiliteDto dto = creneauDisponibiliteConverter.map(found);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Créneau disponibilité non trouvé : " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<CreneauDisponibiliteDto>> getAll() {
        List<CreneauDisponibilite> entities = creneauDisponibiliteService.findAll();
        List<CreneauDisponibiliteDto> dtos = creneauDisponibiliteConverter.mapListEntities(entities);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/active")
    public ResponseEntity<List<CreneauDisponibiliteDto>> getAllActive() {
        List<CreneauDisponibilite> entities = creneauDisponibiliteService.findAllActive();
        List<CreneauDisponibiliteDto> dtos = creneauDisponibiliteConverter.mapListEntities(entities);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/jour/{jour}")
    public ResponseEntity<List<CreneauDisponibiliteDto>> getByJour(@PathVariable DayOfWeek jour) {
        List<CreneauDisponibilite> entities = creneauDisponibiliteService.findByJourSemaine(jour);
        List<CreneauDisponibiliteDto> dtos = creneauDisponibiliteConverter.mapListEntities(entities);
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            int deleted = creneauDisponibiliteService.deleteById(id);
            return ResponseEntity.ok("Créneau disponibilité supprimé avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur : " + e.getMessage());
        }
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<?> activate(@PathVariable Long id) {
        try {
            CreneauDisponibilite activated = creneauDisponibiliteService.activate(id);
            CreneauDisponibiliteDto dto = creneauDisponibiliteConverter.map(activated);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur : " + e.getMessage());
        }
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<?> deactivate(@PathVariable Long id) {
        try {
            CreneauDisponibilite deactivated = creneauDisponibiliteService.deactivate(id);
            CreneauDisponibiliteDto dto = creneauDisponibiliteConverter.map(deactivated);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur : " + e.getMessage());
        }
    }
}
