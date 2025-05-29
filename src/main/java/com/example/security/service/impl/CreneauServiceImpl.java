package com.example.security.service.impl;

import com.example.security.dao.GenerationCreneauxParDefautRepository;
import com.example.security.dao.CreneauDisponibiliteRepository;
import com.example.security.entity.Creneau;
import com.example.security.service.facade.CreneauService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import java.util.stream.Collectors;

@Service
@Transactional
public class CreneauServiceImpl implements CreneauService {


    private final GenerationCreneauxParDefautRepository creneauRepository;

    private final CreneauDisponibiliteRepository creneauDisponibiliteRepository;

    public CreneauServiceImpl(GenerationCreneauxParDefautRepository creneauRepository, CreneauDisponibiliteRepository creneauDisponibiliteRepository) {
        this.creneauRepository = creneauRepository;
        this.creneauDisponibiliteRepository = creneauDisponibiliteRepository;
    }

    @Override
    public Creneau findById(Long id) {
        return creneauRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Creneau non trouvé avec l'ID : " + id));
    }

    @Override
    public List<Creneau> findAll() {
        return creneauRepository.findAll();
    }

    @Override
    public boolean isCreneauAvailable(Long creneauId) {
        Creneau creneau = findById(creneauId);
        return creneau.getActif() && creneau.getCapaciteMax() > 0;
    }

    @Override
    public Creneau reserverCreneau(Long creneauId) {
        Creneau creneau = findById(creneauId);

        if (!creneau.getActif()) {
            throw new RuntimeException("Ce créneau n'est pas actif");
        }

        if (creneau.getCapaciteMax() <= 0) {
            throw new RuntimeException("Ce créneau n'a plus de places disponibles");
        }

        creneau.setCapaciteMax(creneau.getCapaciteMax() - 1);
        return creneauRepository.save(creneau);
    }

    @Override
    public List<Creneau> findAllActiveCreneaux() {
        // Récupère les créneaux actifs avec une capacité disponible > 0
        return creneauRepository.findByActifTrueAndCapaciteMaxGreaterThan(0)
                .stream()
                .filter(creneau -> creneau.getDateCreneau().isAfter(LocalDate.now().minusDays(1)))
                .collect(Collectors.toList());
    }

}