package com.example.security.ws.facade;

import com.example.security.entity.Temoignage;
import com.example.security.service.facade.TemoignageService;
import com.example.security.ws.converter.TemoignageConverter;
import com.example.security.ws.dto.TemoignageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/temoignages")
public class TemoignageController {

    private final TemoignageService temoignageService;
    private final TemoignageConverter temoignageConverter;

    @Autowired
    public TemoignageController(TemoignageService temoignageService, TemoignageConverter temoignageConverter) {
        this.temoignageService = temoignageService;
        this.temoignageConverter = temoignageConverter;
    }

    // ✅ Ajouter un témoignage
    @PostMapping("")
    public TemoignageDto ajouterTemoignage(@RequestBody TemoignageDto dto) {
        Temoignage entity = temoignageConverter.map(dto);
        Temoignage saved = temoignageService.ajouterTemoignage(entity);
        return temoignageConverter.map(saved);
    }

    // ✅ Récupérer la liste de tous les témoignages
    @GetMapping("")
    public List<TemoignageDto> getAllTemoignages() {
        List<Temoignage> temoignages = temoignageService.getAllTemoignages();
        return temoignageConverter.mapListEntities(temoignages);
    }

    // ✅ Supprimer un témoignage par ID
    @DeleteMapping("/{id}")
    public void supprimerTemoignage(@PathVariable Long id) {
        temoignageService.supprimerTemoignage(id);
    }
}
