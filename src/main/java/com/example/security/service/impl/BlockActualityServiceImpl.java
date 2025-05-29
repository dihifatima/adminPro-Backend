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

    private void deleteImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            String filename = imageUrl.replace("/images/", "");
            Path imagePath = Paths.get(uploadDir).resolve(filename);
            try {
                Files.deleteIfExists(imagePath);
                System.out.println("Image supprimée : " + imagePath);
            } catch (IOException e) {
                System.err.println("Erreur lors de la suppression de l'image : " + imagePath + " - " + e.getMessage());
            }
        }
    }

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

            String ancienneImageUrl = aModifier.getImageUrl();

            aModifier.setTitre(actualite.getTitre());
            aModifier.setIntroduction(actualite.getIntroduction());
            aModifier.setAuteur(actualite.getAuteur());
            aModifier.setCategorie(actualite.getCategorie());

            if (actualite.getImageUrl() != null) {
                aModifier.setImageUrl(actualite.getImageUrl());
                // Supprimer l'ancienne image si elle est différente de la nouvelle
                if (ancienneImageUrl != null && !ancienneImageUrl.equals(actualite.getImageUrl())) {
                    deleteImage(ancienneImageUrl);
                }
            }

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
        Optional<BlockActuality> actualiteOpt = blockActualityRepository.findById(id);
        if (actualiteOpt.isPresent()) {
            BlockActuality actualite = actualiteOpt.get();

            deleteImage(actualite.getImageUrl());

            blockActualityRepository.deleteById(id);
        } else {
            throw new RuntimeException("Article introuvable avec l'id : " + id);
        }
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