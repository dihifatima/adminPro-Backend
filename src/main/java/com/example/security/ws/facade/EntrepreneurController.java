package com.example.security.ws.facade;

import com.example.security.Authentification.security.JwtService;
import com.example.security.entity.Entrepreneur;
import com.example.security.service.facade.EntrepreneurService;
import com.example.security.ws.converter.EntrepreneurConverter;
import com.example.security.ws.dto.EntrepreneurDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/entrepreneur")
public class EntrepreneurController {

    @Autowired
    private EntrepreneurService service;

    @Autowired
    private EntrepreneurConverter converter;

    @Autowired
    private JwtService jwtService;

    @PutMapping("/update-complet")
    public ResponseEntity<Integer> updateEtudiantComplet(
            @RequestHeader("Authorization") String token,
            @RequestBody EntrepreneurDto entrepreneurDto
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
                entrepreneurDto.getCin(),
                entrepreneurDto.getNomEntreprise(),
                entrepreneurDto.getSecteurActivite(),
                entrepreneurDto.getRegistreCommerce(),
                entrepreneurDto.getIdentifiantFiscal(),
                entrepreneurDto.getTypeEntreprise(),
                entrepreneurDto.getDateCreation(),
                entrepreneurDto.getSiegeSocial()
        );

        System.out.println("Résultat de l'opération: " + result);

        if (result == -1) {
            return ResponseEntity.notFound().build();  // Étudiant introuvable
        }
        return ResponseEntity.ok(result);
    }


    @GetMapping("email/{email}")
    public ResponseEntity<EntrepreneurDto> findByEmail(@PathVariable String email) {
        Entrepreneur entrepreneur = service.findByEmail(email);
        if (entrepreneur == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        EntrepreneurDto dto = converter.map(entrepreneur);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/admin/id/{id}")
    public int deleteById(@PathVariable Long id) {
        return service.deleteById(id);
    }

    @GetMapping("/admin")
    public List<EntrepreneurDto> findAll() {
        List<Entrepreneur> entites = service.findAll();
        return converter.mapListEntities(entites);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<EntrepreneurDto> getEtudiantById(@PathVariable Long id) {
        Entrepreneur entrepreneur = service.getEntrepreneurById(id);
        if (entrepreneur == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        EntrepreneurDto dto = converter.map(entrepreneur);
        return ResponseEntity.ok(dto);
    }
}