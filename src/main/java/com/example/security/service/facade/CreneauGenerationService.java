package com.example.security.service.facade;


import java.time.LocalDate;

public interface CreneauGenerationService {

    void initializeDefaultCreneauxDisponibilite();

    void generateCreneauxForPeriod(LocalDate dateDebut, LocalDate dateFin);

    void generateCreneauxForNext30Days();

    void regenerateAllFutureCreneaux();

    void updateCreneauxAfterDisponibiliteChange(Long creneauDisponibiliteId);

    void synchronizeCreneauxWithDisponibilite();

    void cleanupPastCreneaux();
}
