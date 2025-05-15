package com.example.security.ws.facade;

import com.example.security.entity.BlocActualites;
import com.example.security.service.facade.BlocActualitesService;
import com.example.security.ws.converter.BlocActualitesConverter;
import com.example.security.ws.dto.BlocActualitesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/actualites")
public class BlocActualitesController {
    private final BlocActualitesService blocActualitesService;
    private final BlocActualitesConverter converter;
     @Autowired
    public BlocActualitesController(BlocActualitesService blocActualitesService, BlocActualitesConverter converter) {
        this.blocActualitesService = blocActualitesService;
        this.converter = converter;
    }
    @PostMapping("/")
    public BlocActualitesDto ajouter(@RequestBody BlocActualitesDto dto) {
        BlocActualites actualite = converter.map(dto);
        BlocActualites saved = blocActualitesService.ajouterActualite(actualite);
        return converter.map(saved);
    }

    @PutMapping("/{id}")
    public BlocActualitesDto modifier(@PathVariable Long id, @RequestBody BlocActualitesDto dto) {
        BlocActualites actualite = converter.map(dto);
        BlocActualites updated = blocActualitesService.modifierActualite(id, actualite);
        return converter.map(updated);
    }

    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        blocActualitesService.supprimerActualite(id);
    }

    @GetMapping("/")
    public List<BlocActualitesDto> getAll() {
        return converter.mapListEntities(blocActualitesService.getAllActualites());
    }

    @GetMapping("/{id}")
    public BlocActualitesDto getById(@PathVariable Long id) {
        Optional<BlocActualites> actualite = blocActualitesService.getActualiteById(id);
        return actualite.map(converter::map).orElse(null);
    }
}

