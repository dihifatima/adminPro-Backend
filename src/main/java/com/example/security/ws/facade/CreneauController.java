package com.example.security.ws.facade;
import com.example.security.entity.Creneau;
import com.example.security.service.facade.CreneauService;
import com.example.security.ws.converter.CreneauConverter;
import com.example.security.ws.dto.CreneauDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/creneaux")
public class CreneauController {

    private final CreneauService creneauService;
    private final CreneauConverter creneauConverter;

    public CreneauController(CreneauService creneauService, CreneauConverter creneauConverter) {
        this.creneauService = creneauService;
        this.creneauConverter = creneauConverter;
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


    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateCreneauStatus(
            @PathVariable Long id,
            @RequestParam("actif") Boolean actif) {
        try {
            Creneau updated = creneauService.updateCreneauStatus(id, actif);
            CreneauDto dto = creneauConverter.map(updated);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<CreneauDto>> getAll() {
        List<Creneau> entities = creneauService.findAll();
        List<CreneauDto> dtos = creneauConverter.mapListEntities(entities);
        return ResponseEntity.ok(dtos);
    }

}