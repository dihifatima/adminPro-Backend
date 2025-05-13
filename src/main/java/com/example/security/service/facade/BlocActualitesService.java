package com.example.security.service.facade;

import com.example.security.entity.BlocActualites;

import java.util.List;
import java.util.Optional;

public interface BlocActualitesService {

    BlocActualites ajouterActualite(BlocActualites actualite);

    BlocActualites modifierActualite(Long id, BlocActualites actualite);

    void supprimerActualite(Long id);

    List<BlocActualites> getAllActualites();

    Optional<BlocActualites> getActualiteById(Long id);
}