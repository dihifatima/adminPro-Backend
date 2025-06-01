package com.example.security.service.facade;
import com.example.security.entity.CreneauDisponibilite;
import java.time.DayOfWeek;
import java.util.List;

public interface ParametrageCreneauDisponibiliteService {

    CreneauDisponibilite update(CreneauDisponibilite creneauDisponibilite);
    List<CreneauDisponibilite> findAll();
    List<CreneauDisponibilite> findByJourSemaine(DayOfWeek jour); //  à ajouter
    int deleteById(Long id);
    CreneauDisponibilite updateActifStatus(Long id, Boolean actif);

}

