package com.example.security.ws.facade;

import com.example.security.entity.Etudiant;
import com.example.security.service.facade.EtudiantService;
import com.example.security.ws.converter.EtudiantConverter;
import com.example.security.ws.dto.EtudiantDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController  // Ajout de l'annotation RestController
@RequestMapping("api/etudiants")  // Ajout du mapping pour le chemin d'API
public class EtudiantController {

    @Autowired
    private EtudiantService service;
    @Autowired
    private EtudiantConverter converter;

    @PostMapping("")
    public int save(@RequestBody EtudiantDto dto) {
        Etudiant etudiant = converter.map(dto);
        return service.save(etudiant);
    }

    @PutMapping("")
    public int update(@RequestBody EtudiantDto dto) {
        Etudiant etudiant = converter.map(dto);
        return service.update(etudiant);
    }

    @GetMapping("email/{email}")
    public EtudiantDto findByEmail(@PathVariable String email) {
        Etudiant commande = service.findByEmail(email);
        EtudiantDto dto = converter.map(commande);
        return dto;
    }

    @DeleteMapping("id/{id}")
    public int deleteByRef(@PathVariable Long id) {
        return service.deleteById(id);
    }

    @GetMapping()
    public List<EtudiantDto> findAll() {
        List<Etudiant> entites = service.findAll();
        List<EtudiantDto> dtos = converter.mapListEntities(entites);
        return dtos;
    }





}
