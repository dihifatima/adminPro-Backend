package com.example.security.ws.facade;

import com.example.security.entity.BlockActuality;
import com.example.security.service.facade.BlockActualityService;
import com.example.security.ws.converter.BlockActualityConverter;
import com.example.security.ws.dto.BlockActualityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/actualites")
public class BlockActualityController {
    private final BlockActualityService blockActualityService;
    private final BlockActualityConverter converter;

    @Autowired
    public BlockActualityController(BlockActualityService blockActualityService, BlockActualityConverter converter) {
        this.blockActualityService = blockActualityService;
        this.converter = converter;
    }

    @PostMapping("/")
    public ResponseEntity<BlockActualityDto> ajouter(
            @RequestPart("actualite") BlockActualityDto dto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) {
        try {
            BlockActuality actualite = converter.map(dto);

            if (imageFile != null && !imageFile.isEmpty()) {
                String filename = blockActualityService.storeImage(imageFile);
                actualite.setImageUrl("/images/" + filename);
            }

            BlockActuality saved = blockActualityService.ajouterActualite(actualite);
            return ResponseEntity.ok(converter.map(saved));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlockActualityDto> modifier(
            @PathVariable Long id,
            @RequestPart("actualite") BlockActualityDto dto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) {
        try {
            BlockActuality actualite = converter.map(dto);

            if (imageFile != null && !imageFile.isEmpty()) {
                String filename = blockActualityService.storeImage(imageFile);
                actualite.setImageUrl("/images/" + filename);
            }

            BlockActuality updated = blockActualityService.modifierActualite(id, actualite);
            return ResponseEntity.ok(converter.map(updated));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        blockActualityService.supprimerActualite(id);
    }

    @GetMapping("/")
    public List<BlockActualityDto> getAll() {
        return converter.mapListEntities(blockActualityService.getAllActualites());
    }

    @GetMapping("/{id}")
    public BlockActualityDto getById(@PathVariable Long id) {
        Optional<BlockActuality> actualite = blockActualityService.getActualiteById(id);
        return actualite.map(converter::map).orElse(null);
    }
}
