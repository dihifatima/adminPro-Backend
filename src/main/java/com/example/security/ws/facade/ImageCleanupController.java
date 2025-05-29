package com.example.security.ws.facade;

import com.example.security.service.impl.ImageCleanupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur d'administration pour le nettoyage manuel des images orphelines.
 */
@RestController
@RequestMapping("/admin/images")
public class ImageCleanupController {

    @Autowired
    private ImageCleanupService imageCleanupService;

    /**
     * Endpoint POST pour déclencher manuellement le nettoyage des images non utilisées.
     */
    @PostMapping("/cleanup")
    public ResponseEntity<String> triggerManualCleanup() {
        imageCleanupService.manualCleanup();
        return ResponseEntity.ok("Nettoyage des images orphelines effectué avec succès.");
    }
}