package com.example.security.service.impl;

import com.example.security.dao.EtudiantRepository;
import com.example.security.entity.Etudiant;
import com.example.security.service.facade.EtudiantService;
import com.example.security.ws.dto.EtudiantDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service

public class EtudiantServiceImpl implements EtudiantService {
    private final EtudiantRepository etudiantRepository;


    public EtudiantServiceImpl(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }

    @Override
    public int save(Etudiant etudiant) {
        if (findByEmail(etudiant.getEmail()) != null) {
            return -1; // étudiant existe déjà
        } else {
            etudiantRepository.save(etudiant);
            return 1; // succès
        }
    }

    @Override
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
    }



    @Override
    public Etudiant findByEmail(String email) {
        /* il est tout à fait logique de rechercher un étudiant par son email, surtout si l'email est unique pour chaque étudiant.
        C'est une pratique courante dans de nombreux systèmes où l'email sert d'identifiant unique pour chaque utilisateur.
         Vous pouvez donc garder la méthode findByEmail sans problème.*/
        return etudiantRepository.findByEmail(email);
    }

    @Override
    @Transactional
    /*
    Méthode deleteById(Long id) :
Cette méthode supprime un étudiant en utilisant son ID.
Elle vérifie d'abord si l'étudiant avec cet ID existe dans la base de données via existsById.
Si l'étudiant n'existe pas, elle retourne -1.
Si l'étudiant existe, il est supprimé via deleteById, et la méthode retourne 1 pour indiquer que la suppression a réussi.
La méthode est annotée avec @Transactional, ce qui garantit que l'opération de suppression est exécutée de manière transactionnelle.
    */
    public int deleteById(Long id) {
        if (!etudiantRepository.existsById(id)) {
            return -1; // ID non trouvé
        } else {
            etudiantRepository.deleteById(id);
            return 1; // suppression réussie
        }
    }
    /*Cette méthode récupère et retourne la liste de tous les étudiants présents
    dans la base de données en utilisant la méthode findAll du EtudiantRepository.*/
    @Override
    public List<Etudiant> findAll() {
        return etudiantRepository.findAll();
    }

    @Override
    public Etudiant getEtudiantById(Long id) {
        return etudiantRepository.findById(id).orElse(null);
    }

    @Override
    public List<Etudiant> findByFirstnameOrLastname(String firstname, String lastname) {
        return etudiantRepository.findByFirstnameOrLastname(firstname, lastname);
    }




}



