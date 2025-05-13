package com.example.security.service.facade;

import com.example.security.entity.Temoignage;

import java.util.List;

public interface TemoignageService {
    Temoignage ajouterTemoignage(Temoignage temoignage);
    List<Temoignage> getAllTemoignages();
    void supprimerTemoignage(Long id);


}
