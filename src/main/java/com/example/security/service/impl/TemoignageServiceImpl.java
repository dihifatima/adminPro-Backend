package com.example.security.service.impl;

import com.example.security.dao.TemoignageRepository;
import com.example.security.entity.Temoignage;
import com.example.security.service.facade.TemoignageService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TemoignageServiceImpl implements TemoignageService {
    private  final TemoignageRepository temoignageRepository;

    public TemoignageServiceImpl(TemoignageRepository temoignageRepository) {
        this.temoignageRepository = temoignageRepository;
    }

    @Override
    public Temoignage ajouterTemoignage(Temoignage temoignage) {
        return temoignageRepository.save(temoignage);
    }

    // Récupérer tous les témoignages
    @Override
    public List<Temoignage> getAllTemoignages() {
        return temoignageRepository.findAll();
    }

    // Supprimer un témoignage par ID
    @Override
    public void supprimerTemoignage(Long id) {
        if (temoignageRepository.existsById(id)) {
            temoignageRepository.deleteById(id);
        } else {
            throw new RuntimeException("Témoignage non trouvé avec l'ID : " + id);
        }
    }
}
