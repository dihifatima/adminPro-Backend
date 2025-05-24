package com.example.security.service.impl;

import com.example.security.dao.BlockActualityRepository;
import com.example.security.entity.BlockActuality;
import com.example.security.entity.BlogSection;
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
            aModifier.setIntroduction(actualite.getIntroduction());
            aModifier.setAuteur(actualite.getAuteur());
            aModifier.setCategorie(actualite.getCategorie());
            aModifier.setImageUrl(actualite.getImageUrl());
            aModifier.setSlug(actualite.getSlug());
            aModifier.setDatePublication(actualite.getDatePublication());
            aModifier.setStatut(actualite.getStatut());
            aModifier.setConclusion(actualite.getConclusion());

            aModifier.setTags(actualite.getTags());

            if (actualite.getSections() != null) {
                aModifier.getSections().clear();

                for (BlogSection section : actualite.getSections()) {
                    section.setBlockActuality(aModifier);
                    aModifier.getSections().add(section);
                }
            }

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
    public Optional<BlockActuality> getActualiteById(Long id) {
        Optional<BlockActuality> actualite = blockActualityRepository.findByIdWithSections(id);
        if (actualite.isEmpty()) {
            System.out.println("L'ID " + id + " n'existe pas.");
        }
        return actualite;
    }

    @Override
    public List<BlockActuality> getAllActualites() {
        return blockActualityRepository.findAllWithSections();
    }

    @Override
    public String storeImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

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

    @Override
    public Optional<BlockActuality> getActualiteWithSections(Long id) {
        return blockActualityRepository.findByIdWithSections(id);
    }

    @Override
    public List<BlockActuality> getAllActualitesWithSections() {
        return blockActualityRepository.findAllWithSections();
    }
}