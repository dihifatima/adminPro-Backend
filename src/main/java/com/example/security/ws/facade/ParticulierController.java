package com.example.security.ws.facade;
import com.example.security.Authentification.security.JwtService;
import com.example.security.entity.Particulier;
import com.example.security.service.facade.ParticulierService;
import com.example.security.ws.converter.ParticulierConverter;
import com.example.security.ws.dto.ParticulierDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/particulier") // CORRECT
public class ParticulierController {
    @Autowired
    private ParticulierService service;

    @Autowired
    private ParticulierConverter converter;

    @Autowired
    private JwtService jwtService;

    @PutMapping("/update-complet")
    public ResponseEntity<Integer> updateParticulierComplet(
            @RequestHeader("Authorization") String token,
            @RequestBody ParticulierDto particulierDto
    ) throws IOException {
        System.out.println("Requête reçue à /update-complet");

        // Enlever "Bearer " du token
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String email = jwtService.extractUsername(token);
        System.out.println("Email extrait du token: " + email);

         int result = service.updateComplet(
                email,                              // 1. Email
                particulierDto.getObjetDemande(),   // 2. ObjetDemande
                particulierDto.getVille(),          // 3. Ville
                particulierDto.getAdresse(),        // 4. Adresse
                particulierDto.getCodeCIN(),        // 5. CodeCIN
                particulierDto.getLieuNaissance(),  // 6. LieuNaissance
                particulierDto.getNationalite(),    // 7. Nationalite
                particulierDto.getGenre(),          // 8. Genre
                particulierDto.getDateNaissance()   // 9. DateNaissance
        );

        System.out.println("Résultat de l'opération: " + result);

        if (result == -1) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }


    @GetMapping("email/{email}")
    public ResponseEntity<ParticulierDto> findByEmail(@PathVariable String email) {
        Particulier particulier = service.findByEmail(email);
        if (particulier == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        ParticulierDto dto = converter.map(particulier);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/admin/id/{id}")
    public int deleteById(@PathVariable Long id) {
        return service.deleteById(id);
    }

    @GetMapping("/admin")
    public List<ParticulierDto> findAll() {
        List<Particulier> entites = service.findAll();
        return converter.mapListEntities(entites);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ParticulierDto> getParticulierById(@PathVariable Long id) {
        Particulier particulier = service.getParticulierById(id);
        if (particulier == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        ParticulierDto dto = converter.map(particulier);
        return ResponseEntity.ok(dto);
    }
}
