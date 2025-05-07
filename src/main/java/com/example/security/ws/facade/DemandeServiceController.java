package com.example.security.ws.facade;

import com.example.security.entity.DemandeService;
import com.example.security.service.facade.DemandeServiceService;
import com.example.security.ws.converter.DemandeServiceConverter;
import com.example.security.ws.dto.DemandeServiceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/demandes")
@CrossOrigin(origins = "http://localhost:4200")
public class DemandeServiceController {
    @Autowired
    private DemandeServiceService demandeServiceService;

    @Autowired
    private DemandeServiceConverter demandeServiceConverter;

    // Créer une nouvelle demande de service
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DemandeServiceDto> createDemandeService(
            @RequestPart("demandeService") DemandeServiceDto demandeServiceDto,
            @RequestPart("file") MultipartFile file
    ) {
        try {
            // Sauvegarde physique du fichier
            String uploadDir = "uploads/";
            Files.createDirectories(Paths.get(uploadDir));

            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Mapper le DTO vers l'entité
            DemandeService demandeService = demandeServiceConverter.map(demandeServiceDto);

            // Associer le chemin du fichier à la demande (si tu as un champ filePath dans DemandeService)
            demandeService.setFilePath(filePath.toString()); // ajoute ce champ dans l'entité si nécessaire

            // Sauvegarder la demande
            DemandeService savedDemandeService = demandeServiceService.save(
                    demandeService,
                    demandeServiceDto.getServiceOffertId(),
                    demandeServiceDto.getUserId()
            );

            if (savedDemandeService != null) {
                return new ResponseEntity<>(demandeServiceConverter.map(savedDemandeService), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // Récupérer une demande de service par son ID
    @GetMapping("/{id}")
    public ResponseEntity<DemandeServiceDto> getDemandeServiceById(@PathVariable Long id) {
        Optional<DemandeService> demandeServiceOpt = demandeServiceService.findById(id);
        if (demandeServiceOpt.isPresent()) {
            return new ResponseEntity<>(demandeServiceConverter.map(demandeServiceOpt.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Récupérer toutes les demandes de services
    @GetMapping
    public ResponseEntity<List<DemandeServiceDto>> getAllDemandesService() {
        List<DemandeService> demandesServices = demandeServiceService.findAll();
        return new ResponseEntity<>(demandeServiceConverter.mapListEntities(demandesServices), HttpStatus.OK);
    }

    // Mettre à jour le statut d'une demande de service
    @PutMapping("/{id}")
    public ResponseEntity<DemandeServiceDto> updateStatut(
            @PathVariable Long id,
            @RequestParam String nouveauStatut) {
        DemandeService updatedDemandeService = demandeServiceService.updateStatut(id, nouveauStatut);
        if (updatedDemandeService != null) {
            return new ResponseEntity<>(demandeServiceConverter.map(updatedDemandeService), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Supprimer une demande de service
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDemandeService(@PathVariable Long id) {
        demandeServiceService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Récupérer les demandes de service par utilisateur
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DemandeServiceDto>> getDemandesByUser(@PathVariable Long userId) {
        List<DemandeService> demandesServices = demandeServiceService.findByUserId(userId);
        return new ResponseEntity<>(demandeServiceConverter.mapListEntities(demandesServices), HttpStatus.OK);
    }

    // Récupérer les demandes de service par service offert
    @GetMapping("/service/{serviceOffertId}")
    public ResponseEntity<List<DemandeServiceDto>> getDemandesByServiceOffert(@PathVariable Long serviceOffertId) {
        List<DemandeService> demandesServices = demandeServiceService.findByServiceOffertId(serviceOffertId);
        return new ResponseEntity<>(demandeServiceConverter.mapListEntities(demandesServices), HttpStatus.OK);
    }
}