package com.example.security.service.impl;

import com.example.security.dao.BlockActualityRepository;
import com.example.security.entity.BlockActuality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ImageCleanupService {

    @Value("${app.upload.dir:${user.home}/uploads/images}")
    private String uploadDir;

    @Autowired
    private BlockActualityRepository blockActualityRepository;

    /**
     * Nettoie les images orphelines tous les jours à 2h du matin
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupOrphanedImages() {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                return;
            }

            // Récupérer toutes les URLs d'images utilisées
            Set<String> usedImages = getUsedImageFilenames();

            // Parcourir les fichiers dans le répertoire
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(uploadPath)) {
                for (Path file : stream) {
                    if (Files.isRegularFile(file)) {
                        String filename = file.getFileName().toString();

                        // Si le fichier n'est pas utilisé, le supprimer
                        if (!usedImages.contains(filename)) {
                            Files.deleteIfExists(file);
                            System.out.println("Image orpheline supprimée : " + filename);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du nettoyage des images orphelines : " + e.getMessage());
        }
    }

    /**
     * Récupère tous les noms de fichiers d'images utilisés dans la base de données
     */
    private Set<String> getUsedImageFilenames() {
        List<BlockActuality> actualites = blockActualityRepository.findAll();
        Set<String> usedImages = new HashSet<>();

        for (BlockActuality actualite : actualites) {
            String imageUrl = actualite.getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                // Extraire le nom du fichier de l'URL
                String filename = imageUrl.replace("/images/", "");
                usedImages.add(filename);
            }
        }

        return usedImages;
    }

    /**
     * Méthode publique pour déclencher manuellement le nettoyage
     */
    public void manualCleanup() {
        cleanupOrphanedImages();
    }
}