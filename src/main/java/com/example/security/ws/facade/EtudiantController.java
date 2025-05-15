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
            @RequestParam(required = false) String niveauEtude,
            @RequestParam(required = false) String filiere,
            @RequestParam(required = false) String etablissementActuel,
            @RequestParam(required = false) MultipartFile scanBac,
            @RequestParam(required = false) MultipartFile cinScan,
            @RequestParam(required = false) MultipartFile photos,
            @RequestParam(required = false) MultipartFile releveDeNotesScan
    ) throws IOException {
        System.out.println("Requête reçue à /update-complet");
        System.out.println("niveauEtude: " + niveauEtude);
        System.out.println("filiere: " + filiere);
        System.out.println("etablissementActuel: " + etablissementActuel);
        System.out.println("scanBac présent: " + (scanBac != null && !scanBac.isEmpty()));
        System.out.println("cinScan présent: " + (cinScan != null && !cinScan.isEmpty()));
        System.out.println("photos présent: " + (photos != null && !photos.isEmpty()));
        System.out.println("releveNotesScan présent: " + (releveDeNotesScan != null && !releveDeNotesScan.isEmpty()));

        // Enlever "Bearer " du token
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String email = jwtService.extractUsername(token);
        System.out.println("Email extrait du token: " + email);

        int result = service.updateComplet(email, niveauEtude, filiere, etablissementActuel,
                scanBac, cinScan, photos, releveDeNotesScan);

        System.out.println("Résultat de l'opération: " + result);

        if (result == -1) {
            return ResponseEntity.notFound().build();  // Étudiant introuvable
        }

        return ResponseEntity.ok(result);
    }
    @GetMapping("/admin/fichier/{nomFichier}")
    public ResponseEntity<byte[]> telechargerFichier(@PathVariable String nomFichier) throws IOException {
        Path chemin = Paths.get("uploads/etudiants").resolve(nomFichier).normalize();

        if (!Files.exists(chemin)) {
            return ResponseEntity.notFound().build();
        }

        byte[] contenu = Files.readAllBytes(chemin);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + nomFichier + "\"")
                .body(contenu);
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
    public int deleteByRef(@PathVariable Long id) {
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


}