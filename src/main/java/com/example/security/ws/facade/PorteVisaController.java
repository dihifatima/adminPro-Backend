package com.example.security.ws.facade;

import com.example.security.Authentification.security.JwtService;
import com.example.security.entity.PorteVisa;
import com.example.security.service.facade.PorteVisaService;
import com.example.security.ws.converter.PorteVisaConverter;
import com.example.security.ws.dto.PorteVisaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/porteVisa")
public class PorteVisaController {
    @Autowired
    private PorteVisaService service;

    @Autowired
    private PorteVisaConverter converter;

    @Autowired
    private JwtService jwtService;

    @PutMapping("/update-complet")
    public ResponseEntity<Integer> updateComplet(
            @RequestHeader("Authorization") String token, @RequestBody PorteVisaDto porteVisaDto
    ) throws IOException {
        System.out.println("Requête reçue à /update-complet");

        // Enlever "Bearer " du token
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String email = jwtService.extractUsername(token);
        System.out.println("Email extrait du token: " + email);

        // Update the call to match the service interface method signature
        int result = service.updateComplet(
                email,
                porteVisaDto.getCodeCIN(),
                porteVisaDto.getDateNaissance(),
                porteVisaDto.getPassportNumber(),
                porteVisaDto.getLieuNaissance(),
                porteVisaDto.getAdresse(),
                porteVisaDto.getGenre(),
                porteVisaDto.getNationalite(),
                porteVisaDto.getDestinationVisa(),
                porteVisaDto.getTypeVisa(),
                porteVisaDto.getDureeSejour(),
                porteVisaDto.getDateDelivrancePassport(),
                porteVisaDto.getDateExpirationPassport()
        );

        System.out.println("Résultat de l'opération: " + result);

        if (result == -1) {
            return ResponseEntity.notFound().build();  // Étudiant introuvable
        }

        return ResponseEntity.ok(result);
    }


    @GetMapping("email/{email}")
    public ResponseEntity<PorteVisaDto> findByEmail(@PathVariable String email) {
        PorteVisa porteVisa = service.findByEmail(email);
        if (porteVisa == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        PorteVisaDto dto = converter.map(porteVisa);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/admin/id/{id}")
    public int deleteById(@PathVariable Long id) {
        return service.deleteById(id);
    }

    @GetMapping("/admin")
    public List<PorteVisaDto> findAll() {
        List<PorteVisa> entites = service.findAll();
        return converter.mapListEntities(entites);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<PorteVisaDto> getPorteVisaById(@PathVariable Long id) {
        PorteVisa porteVisa = service.getPorteVisaById(id);
        if (porteVisa == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        PorteVisaDto dto = converter.map(porteVisa);
        return ResponseEntity.ok(dto);
    }
}