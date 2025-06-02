package com.example.security.service.impl;

import com.example.security.dao.CreneauxRepository;
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


    private final CreneauxRepository creneauRepository;

    private final CreneauDisponibiliteRepository creneauDisponibiliteRepository;

    public CreneauServiceImpl(CreneauxRepository creneauRepository, CreneauDisponibiliteRepository creneauDisponibiliteRepository) {
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
        return creneau.getActif() && creneau.getCapaciteRestante() > 0;
    }

    @Override
    public Creneau updateCreneauStatus(Long creneauId, Boolean actif) {
        Creneau creneau = findById(creneauId);
        creneau.setActif(actif);
        return creneauRepository.save(creneau);
    }

    @Override
    public List<Creneau> findAvailableCreneauxForBooking() {
        return creneauRepository.findByActifTrueAndCapaciteRestanteGreaterThan(0)
                .stream()
                .filter(creneau -> {
                    // Vérifier que le créneau est dans le futur
                    LocalDate today = LocalDate.now();
                    return creneau.getDateCreneau().isAfter(today) ||
                            creneau.getDateCreneau().equals(today);
                })
                .filter(creneau -> {
                    // Vérifier que le CreneauDisponibilite parent est aussi actif
                    return creneau.getCreneauDisponibilite() != null &&
                            creneau.getCreneauDisponibilite().getActif();
                })
                .collect(Collectors.toList());
    }

    @Override
    public Creneau reserverCreneau(Long creneauId) {
        Creneau creneau = findById(creneauId);

        if (!creneau.getActif()) {
            throw new RuntimeException("Ce créneau n'est pas actif");
        }

        if (creneau.getCapaciteRestante() <= 0) {
            throw new RuntimeException("Ce créneau n'a plus de places disponibles");
        }

        creneau.setCapaciteRestante(creneau.getCapaciteRestante() - 1);
        return creneauRepository.save(creneau);
    }



}