package com.example.security.ws.facade;
import com.example.security.Authentification.security.JwtService;
import com.example.security.entity.DemandeurVisa;
import com.example.security.service.facade.DemandeurVisaService;
import com.example.security.ws.converter.DemandeurVisaConverter;
import com.example.security.ws.dto.DemandeurVisaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/porteVisa")
public class DemandeurVisaController {
    @Autowired
    private DemandeurVisaService service;

    @Autowired
    private DemandeurVisaConverter converter;

    @Autowired
    private JwtService jwtService;

    @PutMapping("/update-complet")
    public ResponseEntity<Integer> updateComplet(
            @RequestHeader("Authorization") String token, @RequestBody DemandeurVisaDto demandeurVisaDto
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
                demandeurVisaDto.getCodeCIN(),
                demandeurVisaDto.getDateNaissance(),
                demandeurVisaDto.getPassportNumber(),
                demandeurVisaDto.getLieuNaissance(),
                demandeurVisaDto.getAdresse(),
                demandeurVisaDto.getGenre(),
                demandeurVisaDto.getNationalite(),
                demandeurVisaDto.getDestinationVisa(),
                demandeurVisaDto.getTypeVisa(),
                demandeurVisaDto.getDureeSejour(),
                demandeurVisaDto.getDateDelivrancePassport(),
                demandeurVisaDto.getDateExpirationPassport()
        );

        System.out.println("Résultat de l'opération: " + result);

        if (result == -1) {
            return ResponseEntity.notFound().build();  // Étudiant introuvable
        }

        return ResponseEntity.ok(result);
    }


    @GetMapping("email/{email}")
    public ResponseEntity<DemandeurVisaDto> findByEmail(@PathVariable String email) {
        DemandeurVisa demandeurVisaDto = service.findByEmail(email);
        if (demandeurVisaDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        DemandeurVisaDto dto = converter.map(demandeurVisaDto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/admin/id/{id}")
    public int deleteById(@PathVariable Long id) {
        return service.deleteById(id);
    }

    @GetMapping("/admin")
    public List<DemandeurVisaDto> findAll() {
        List<DemandeurVisa> entites = service.findAll();
        return converter.mapListEntities(entites);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<DemandeurVisaDto> getPorteVisaById(@PathVariable Long id) {
        DemandeurVisa demandeurVisaDto = service.getDemandeurVisaById(id);
        if (demandeurVisaDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        DemandeurVisaDto dto = converter.map(demandeurVisaDto);
        return ResponseEntity.ok(dto);
    }
}