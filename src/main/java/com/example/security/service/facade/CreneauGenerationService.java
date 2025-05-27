package com.example.security.service.facade;
public interface CreneauGenerationService {

    void initializeDefaultCreneauxDisponibilite();
    void regenerateAllFutureCreneaux();
    void cleanupPastCreneaux();

    void generateFutureCreneaux();
}
