package com.example.security.service.facade;
public interface GenerationCreneauxParDefautService {

    void initializeDefaultCreneauxDisponibilite();
    void regenerateAllFutureCreneaux();
    void cleanupPastCreneaux();
    void generateFutureCreneaux();
}
