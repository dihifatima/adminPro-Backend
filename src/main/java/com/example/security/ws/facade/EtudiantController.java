package com.example.security.ws.facade;

import com.example.security.Authentification.security.JwtService;
import com.example.security.entity.Etudiant;
import com.example.security.service.facade.EtudiantService;
import com.example.security.ws.converter.EtudiantConverter;
import com.example.security.ws.dto.EtudiantDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/etudiants")
public class EtudiantController {

    @Autowired
    private EtudiantService service;

    @Autowired
    private EtudiantConverter converter;
    @Autowired
    private JwtService jwtService;


    @PutMapping("/update-complet")
    public ResponseEntity<Integer> updateEtudiantComplet(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String niveauEtude,
            @RequestParam(required = false) String filiere,
            @RequestParam(required = false) String etablissementActuel,
            @RequestParam(required = false) MultipartFile scanBac,
            @RequestParam(required = false) MultipartFile cinScan,
            @RequestParam(required = false) MultipartFile photos,
            @RequestParam(required = false) MultipartFile releveNotes
    ) throws IOException {
        // Enlever "Bearer " du token
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String email = jwtService.extractUsername(token); // C’est ton email ici

        int result = service.updateComplet(email, niveauEtude, filiere, etablissementActuel,
                scanBac, cinScan, photos, releveNotes);

        if (result == -1) {
            return ResponseEntity.notFound().build();  // Étudiant introuvable
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("email/{email}")
    public ResponseEntity<EtudiantDto> findByEmail(@PathVariable String email) {
        Etudiant etudiant = service.findByEmail(email);
        if (etudiant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        EtudiantDto dto = converter.map(etudiant);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("id/{id}")
    public int deleteByRef(@PathVariable Long id) {
        return service.deleteById(id);
    }

    @GetMapping()
    public List<EtudiantDto> findAll() {
        List<Etudiant> entites = service.findAll();
        return converter.mapListEntities(entites);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<EtudiantDto> getEtudiantById(@PathVariable Long id) {
        Etudiant etudiant = service.getEtudiantById(id);
        if (etudiant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        EtudiantDto dto = converter.map(etudiant);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/search")
    public List<EtudiantDto> searchEtudiants(@RequestParam String firstname, @RequestParam String lastname) {
        List<Etudiant> etudiants = service.findByFirstnameOrLastname(firstname, lastname);
        return converter.mapListEntities(etudiants);
    }
}