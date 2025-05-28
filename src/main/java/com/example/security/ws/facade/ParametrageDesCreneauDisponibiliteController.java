package com.example.security.ws.facade;
import com.example.security.entity.CreneauDisponibilite;
import com.example.security.service.facade.CreneauDisponibiliteService;
import com.example.security.ws.converter.CreneauDisponibiliteConverter;
import com.example.security.ws.dto.CreneauDisponibiliteDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/creneaux-disponibilite")
public class ParametrageDesCreneauDisponibiliteController {

    private final  CreneauDisponibiliteService creneauDisponibiliteService;

    private  final CreneauDisponibiliteConverter creneauDisponibiliteConverter;

    public ParametrageDesCreneauDisponibiliteController(CreneauDisponibiliteService creneauDisponibiliteService, CreneauDisponibiliteConverter creneauDisponibiliteConverter) {
        this.creneauDisponibiliteService = creneauDisponibiliteService;
        this.creneauDisponibiliteConverter = creneauDisponibiliteConverter;
    }

    /*@PostMapping("/create")
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
*/
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


    @GetMapping("/all")
    public ResponseEntity<List<CreneauDisponibiliteDto>> getAll() {
        List<CreneauDisponibilite> entities = creneauDisponibiliteService.findAll();
        List<CreneauDisponibiliteDto> dtos = creneauDisponibiliteConverter.mapListEntities(entities);
        return ResponseEntity.ok(dtos);
    }


// pour voir planing de jour-http://localhost:8080/api/v1/creneaux-disponibilite/jour/MONDAY
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
            return ResponseEntity.ok("Le créneau de disponibilité avec l'ID " + id + " a été supprimé avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur : " + e.getMessage());
        }
    }




}