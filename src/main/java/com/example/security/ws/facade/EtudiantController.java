package com.example.security.ws.facade;

import com.example.security.Authentification.security.JwtService;
import com.example.security.entity.Etudiant;
import com.example.security.service.facade.EtudiantService;
import com.example.security.ws.converter.EtudiantConverter;
import com.example.security.ws.dto.EtudiantDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/etudiants")
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
            @RequestBody EtudiantDto etudiantDto
    ) throws IOException {
        System.out.println("Requête reçue à /update-complet");

        // Enlever "Bearer " du token
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String email = jwtService.extractUsername(token);
        System.out.println("Email extrait du token: " + email);

        int result = service.updateComplet(
                email,
                etudiantDto.getCodeMassar(),
                etudiantDto.getCodeCIN(),
                etudiantDto.getDateNaissance(),
                etudiantDto.getLieuNaissance(),
                etudiantDto.getAdresse(),
                etudiantDto.getGenre(),
                etudiantDto.getNationalite(),
                etudiantDto.getNiveauScolaire(),
                etudiantDto.getMentionBac(),
                etudiantDto.getTypeBac(),
                etudiantDto.getAnneeBac()
        );

        System.out.println("Résultat de l'opération: " + result);

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

    @DeleteMapping("/admin/id/{id}")
    public int deleteById(@PathVariable Long id) {
        return service.deleteById(id);
    }

    @GetMapping("/admin")
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
    @GetMapping("email/{email}/clientId")
    public ResponseEntity<Long> findClientIdByEmail(@PathVariable String email) {
        Etudiant etudiant = service.findByEmail(email);
        if (etudiant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(etudiant.getId());
    }
}