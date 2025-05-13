package com.example.security.service.impl;

import com.example.security.dao.BlocActualitesRepository;
import com.example.security.entity.BlocActualites;
import com.example.security.service.facade.BlocActualitesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class BlocActualitesServiceImpl implements BlocActualitesService {
    private final BlocActualitesRepository actualitesRepository;

    @Autowired
    public BlocActualitesServiceImpl(BlocActualitesRepository actualitesRepository) {
        this.actualitesRepository = actualitesRepository;
    }

    @Override
    public BlocActualites ajouterActualite(BlocActualites actualite) {
        return actualitesRepository.save(actualite);
    }

    @Override
    public BlocActualites modifierActualite(Long id, BlocActualites actualite) {
        Optional<BlocActualites> existante = actualitesRepository.findById(id);
        if (existante.isPresent()) {
            BlocActualites aModifier = existante.get();
            aModifier.setTitre(actualite.getTitre());
            aModifier.setResume(actualite.getResume());
            aModifier.setContenu(actualite.getContenu());
            aModifier.setAuteur(actualite.getAuteur());
            aModifier.setCategorie(actualite.getCategorie());
            aModifier.setImageUrl(actualite.getImageUrl());
            aModifier.setSlug(actualite.getSlug());
            aModifier.setDatePublication(actualite.getDatePublication());
            return actualitesRepository.save(aModifier);
        } else {
            throw new RuntimeException("Article introuvable avec l'id : " + id);
        }
    }

    @Override
    public void supprimerActualite(Long id) {
        actualitesRepository.deleteById(id);
    }

    @Override
    public List<BlocActualites> getAllActualites() {
        return actualitesRepository.findAll();
    }

    @Override
    public Optional<BlocActualites> getActualiteById(Long id) {
        return actualitesRepository.findById(id);
    }
}