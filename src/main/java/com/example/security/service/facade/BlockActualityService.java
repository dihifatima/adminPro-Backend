package com.example.security.service.facade;

import com.example.security.entity.BlockActuality;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface BlockActualityService {

    BlockActuality ajouterActualite(BlockActuality actualite);
    BlockActuality modifierActualite(Long id, BlockActuality actualite);
    void supprimerActualite(Long id);
    List<BlockActuality> getAllActualites();
    Optional<BlockActuality> getActualiteById(Long id);

    // Méthodes pour la gestion des images
    String storeImage(MultipartFile file) throws IOException;

    // Méthodes pour les statistiques
    void incrementViewCount(Long id);

    // Nouvelles méthodes pour optimiser les performances
    Optional<BlockActuality> getActualiteWithSections(Long id);
    List<BlockActuality> getAllActualitesWithSections();
}