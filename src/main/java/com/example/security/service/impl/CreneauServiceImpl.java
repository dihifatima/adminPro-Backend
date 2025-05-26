package com.example.security.service.impl;

import com.example.security.dao.CreneauRepository;
import com.example.security.entity.Creneau;
import com.example.security.service.facade.CreneauDisponibiliteService;
import com.example.security.service.facade.CreneauService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class CreneauServiceImpl implements CreneauService {
    @Autowired
    private final CreneauRepository creneauRepository;
    @Autowired
    private final CreneauDisponibiliteService creneauDisponibiliteService;

    public CreneauServiceImpl(CreneauRepository creneauRepository, CreneauDisponibiliteService creneauDisponibiliteService) {
        this.creneauRepository = creneauRepository;
        this.creneauDisponibiliteService = creneauDisponibiliteService;
    }

    @Override
    public Creneau save(Creneau creneau) {
        if (creneau == null) {
            throw new RuntimeException("Creneau ne peut pas être null");
        }

        validateCreneau(creneau);
        return creneauRepository.save(creneau);
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

    @Override
    public Creneau libererCreneau(Long creneauId) {
        Creneau creneau = findById(creneauId);

        // Vérifier qu'on ne dépasse pas la capacité maximale
        int capaciteMax = creneau.getCreneauDisponibilite().getCapaciteMax();
        if (creneau.getCapaciteRestante() >= capaciteMax) {
            throw new RuntimeException("La capacité maximale est déjà atteinte");
        }

        creneau.setCapaciteRestante(creneau.getCapaciteRestante() + 1);
        return creneauRepository.save(creneau);
    }
    private void validateCreneau(Creneau creneau) {
        if (creneau.getDateCreneau() == null) {
            throw new RuntimeException("La date du créneau est obligatoire");
        }

        if (creneau.getHeureDebut() == null || creneau.getHeureFin() == null) {
            throw new RuntimeException("Les heures de début et fin sont obligatoires");
        }

        if (creneau.getHeureDebut().isAfter(creneau.getHeureFin()) ||
                creneau.getHeureDebut().equals(creneau.getHeureFin())) {
            throw new RuntimeException("L'heure de début doit être antérieure à l'heure de fin");
        }

        if (creneau.getCapaciteRestante() == null || creneau.getCapaciteRestante() < 0) {
            throw new RuntimeException("La capacité restante ne peut pas être négative");
        }

        if (creneau.getCreneauDisponibilite() != null &&
                creneau.getCapaciteRestante() > creneau.getCreneauDisponibilite().getCapaciteMax()) {
            throw new RuntimeException("La capacité restante ne peut pas dépasser la capacité maximale");
        }

        // Vérifier que la date n'est pas dans le passé (sauf pour les mises à jour)
        if (creneau.getId() == null && creneau.getDateCreneau().isBefore(LocalDate.now())) {
            throw new RuntimeException("Impossible de créer un créneau dans le passé");
        }
    }
}
