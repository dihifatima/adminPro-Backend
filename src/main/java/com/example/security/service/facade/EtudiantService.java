package com.example.security.service.facade;

import com.example.security.entity.Etudiant;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EtudiantService {

    /*@Override
    public int update(Etudiant etudiant) {
        Etudiant existing = findByEmail(etudiant.getEmail());
        if (existing == null) return -1;

        // Mise à jour uniquement des champs non nuls
        if (etudiant.getFirstname() != null) existing.setFirstname(etudiant.getFirstname());
        if (etudiant.getLastname() != null) existing.setLastname(etudiant.getLastname());
        if (etudiant.getNiveauEtude() != null) existing.setNiveauEtude(etudiant.getNiveauEtude());
        if (etudiant.getFiliere() != null) existing.setFiliere(etudiant.getFiliere());
        if (etudiant.getEtablissementActuel() != null) existing.setEtablissementActuel(etudiant.getEtablissementActuel());
        if (etudiant.getScanBacPath() != null) existing.setScanBacPath(etudiant.getScanBacPath());
        if (etudiant.getCinScanPath() != null) existing.setCinScanPath(etudiant.getCinScanPath());
        if (etudiant.getPhotos() != null) existing.setPhotos(etudiant.getPhotos());
        if (etudiant.getReleveDeNotesScanPath() != null) existing.setReleveDeNotesScanPath(etudiant.getReleveDeNotesScanPath());

        etudiantRepository.save(existing);
        return 0;
    }*/
    int updateComplet(String email, String niveauEtude, String filiere, String etablissementActuel,
                      MultipartFile scanBac, MultipartFile cinScan, MultipartFile photos, MultipartFile releveDeNotesScan) throws IOException;

    Etudiant findByEmail(String email);
    int deleteById(Long id);
    List<Etudiant> findAll();//Récupérer tous les étudiants.
    Etudiant getEtudiantById(Long id); //Récupérer un étudiant par ID.


}
