package com.example.security.ws.facade;

import com.example.security.entity.DemandeService;
import com.example.security.entity.EtatDemande;
import com.example.security.service.facade.DemandeServiceService;
import com.example.security.ws.converter.DemandeServiceConverter;
import com.example.security.ws.dto.DemandeServiceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/demandes")
@CrossOrigin(origins = "http://localhost:4200")
public class DemandeServiceController {

    @Autowired
    private  DemandeServiceService demandeServiceService;
    @Autowired
    private DemandeServiceConverter demandeServiceConverter;

    // ✅ utilise 'create' au lieu de 'save'
    @PostMapping("/")
    public ResponseEntity<DemandeServiceDto> create(@RequestBody DemandeServiceDto demandeDto) {
        DemandeService demande = demandeServiceConverter.map(demandeDto);
        DemandeService savedDemande = demandeServiceService.create(demande);
        return ResponseEntity.ok(demandeServiceConverter.map(savedDemande));
    }

    // ✅ met à jour uniquement l'état (conforme à updateStatus)
    @PutMapping("/{id}")
    public ResponseEntity<DemandeServiceDto> updateStatus(@PathVariable Long id, @RequestBody DemandeServiceDto demandeDto) {
        EtatDemande etat = EtatDemande.valueOf(demandeDto.getEtat());
        DemandeService updatedDemande = demandeServiceService.updateStatus(id, etat);
        return ResponseEntity.ok(demandeServiceConverter.map(updatedDemande));
    }

    @GetMapping("/")
    public ResponseEntity<List<DemandeServiceDto>> findAll() {
        List<DemandeService> demandes = demandeServiceService.findAll();
        return ResponseEntity.ok(demandeServiceConverter.mapListEntities(demandes));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<DemandeServiceDto> findById(@PathVariable Long id) {
        DemandeService demande = demandeServiceService.findById(id);
        return ResponseEntity.ok(demandeServiceConverter.map(demande));
    }

    // ✅ utilise 'delete' au lieu de 'deleteById'
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        demandeServiceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
