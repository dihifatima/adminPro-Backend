package com.example.security.service.impl;

import com.example.security.dao.BlockActualityRepository;
import com.example.security.entity.BlockActuality;
import com.example.security.service.facade.BlockActualityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BlockActualityServiceImpl implements BlockActualityService {
    private final BlockActualityRepository blockActualityRepository;

    @Value("${app.upload.dir:${user.home}/uploads/images}")
    private String uploadDir;

    @Autowired
    public BlockActualityServiceImpl(BlockActualityRepository actualitesRepository) {
        this.blockActualityRepository = actualitesRepository;
    }

    @Override
    public BlockActuality ajouterActualite(BlockActuality actualite) {
        return blockActualityRepository.save(actualite);
    }

    @Override
    public BlockActuality modifierActualite(Long id, BlockActuality actualite) {
        Optional<BlockActuality> existante = blockActualityRepository.findById(id);
        if (existante.isPresent()) {
            BlockActuality aModifier = existante.get();

            aModifier.setTitre(actualite.getTitre());
            aModifier.setAuteur(actualite.getAuteur());
            aModifier.setCategorie(actualite.getCategorie());
            aModifier.setImageUrl(actualite.getImageUrl());
            aModifier.setImageName(actualite.getImageName());
            aModifier.setSlug(actualite.getSlug());
            aModifier.setDatePublication(actualite.getDatePublication());
            aModifier.setStatut(actualite.getStatut());
            aModifier.setTags(actualite.getTags());

            // Contenu structuré
            aModifier.setIntroduction(actualite.getIntroduction());
            aModifier.setTitre1(actualite.getTitre1());
            aModifier.setSection1(actualite.getSection1());
            aModifier.setTitre2(actualite.getTitre2());
            aModifier.setSection2(actualite.getSection2());
            aModifier.setTitre3(actualite.getTitre3());
            aModifier.setSection3(actualite.getSection3());
            aModifier.setTitre4(actualite.getTitre4());
            aModifier.setSection4(actualite.getSection4());
            aModifier.setConclusion(actualite.getConclusion());

            // Optionnel : mise à jour du compteur de vues
            aModifier.setViewCount(actualite.getViewCount());
            return blockActualityRepository.save(aModifier);
        } else {
            throw new RuntimeException("Article introuvable avec l'id : " + id);
        }
    }

    @Override
    public void supprimerActualite(Long id) {
        blockActualityRepository.deleteById(id);
    }

    @Override
    public List<BlockActuality> getAllActualites() {
        return blockActualityRepository.findAll();
    }

    @Override
    public Optional<BlockActuality> getActualiteById(Long id) {
        Optional<BlockActuality> actualite = blockActualityRepository.findById(id);
        if (actualite.isEmpty()) {
            System.out.println("L'ID " + id + " n'existe pas.");
        }
        return actualite;
    }

    @Override
    public String storeImage(MultipartFile file) throws IOException {
        // Créer le répertoire s'il n'existe pas
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Générer un nom de fichier unique pour éviter les conflits
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Copier le fichier dans le répertoire cible
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }

    @Override
    public void incrementViewCount(Long id) {
        Optional<BlockActuality> actualite = blockActualityRepository.findById(id);
        actualite.ifPresent(a -> {
            a.incrementViewCount();
            blockActualityRepository.save(a);
        });
    }
}