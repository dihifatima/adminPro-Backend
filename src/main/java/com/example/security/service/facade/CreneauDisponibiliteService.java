package com.example.security.service.facade;

import com.example.security.entity.CreneauDisponibilite;

import java.time.DayOfWeek;
import java.util.List;

public interface CreneauDisponibiliteService {

    List<CreneauDisponibilite> findAllActive();

    List<CreneauDisponibilite> findByJourSemaine(DayOfWeek jourSemaine);

    int deleteById(Long id);

    CreneauDisponibilite save(CreneauDisponibilite creneauDisponibilite);

    CreneauDisponibilite update(CreneauDisponibilite creneauDisponibilite);

    CreneauDisponibilite findById(Long id);

    List<CreneauDisponibilite> findAll();

    CreneauDisponibilite activate(Long id);

    CreneauDisponibilite deactivate(Long id);
}
