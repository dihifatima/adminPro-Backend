package com.example.security.ws.facade;
import com.example.security.entity.CreneauDisponibilite;
import com.example.security.service.facade.ParametrageCreneauDisponibiliteService;
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
public class ParametrageDesCreneauxDisponibiliteController {

    private final ParametrageCreneauDisponibiliteService parametrageCreneauDisponibiliteService;

    private  final CreneauDisponibiliteConverter creneauDisponibiliteConverter;

    public ParametrageDesCreneauxDisponibiliteController(ParametrageCreneauDisponibiliteService creneauDisponibiliteService, CreneauDisponibiliteConverter creneauDisponibiliteConverter) {
        this.parametrageCreneauDisponibiliteService = creneauDisponibiliteService;
        this.creneauDisponibiliteConverter = creneauDisponibiliteConverter;
    }
// hada bache ibde creneau kamle
    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody CreneauDisponibiliteDto dto) {
        try {
            CreneauDisponibilite entity = creneauDisponibiliteConverter.map(dto);
            CreneauDisponibilite updated = parametrageCreneauDisponibiliteService.update(entity);
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

    // Modification uniquement du champ 'actif' et aussi si j ai modifier la disponibilite de ce creneuxDisponibilite elle va etre aussi  modifier dans les creneaux liee !!!!!
    @PutMapping("updateStatuDeCreneau/{id}/actif")
    public ResponseEntity<?> updateActifStatus(
            @PathVariable Long id,
            @RequestParam("value") Boolean actif
    ) {
        try {
            CreneauDisponibilite updated = parametrageCreneauDisponibiliteService.updateActifStatus(id, actif);
            CreneauDisponibiliteDto updatedDto = creneauDisponibiliteConverter.map(updated);
            return ResponseEntity.ok(updatedDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur : " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur inattendue : " + e.getMessage());
        }
    }

    //hadi kat3ti ghir les crenaux no date
    @GetMapping("/all")
    public ResponseEntity<List<CreneauDisponibiliteDto>> getAll() {
        List<CreneauDisponibilite> entities = parametrageCreneauDisponibiliteService.findAll();
        List<CreneauDisponibiliteDto> dtos = creneauDisponibiliteConverter.mapListEntities(entities);
        return ResponseEntity.ok(dtos);
    }


// pour voir planing de jour-http://localhost:8080/api/v1/creneaux-disponibilite/jour/MONDAY
    @GetMapping("/jour/{jour}")
    public ResponseEntity<List<CreneauDisponibiliteDto>> getByJour(@PathVariable DayOfWeek jour) {
        List<CreneauDisponibilite> entities = parametrageCreneauDisponibiliteService.findByJourSemaine(jour);
        List<CreneauDisponibiliteDto> dtos = creneauDisponibiliteConverter.mapListEntities(entities);
        return ResponseEntity.ok(dtos);
    }
// pour ramadan bache i hyde dok heure libegha ????
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            int deleted = parametrageCreneauDisponibiliteService.deleteById(id);
            return ResponseEntity.ok("Le créneau de disponibilité avec l'ID " + id + " a été supprimé avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur : " + e.getMessage());
        }
    }


}