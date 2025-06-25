package com.example.security.service.impl;

import com.example.security.dao.EtudiantRepository;
import com.example.security.entity.Etudiant;
import com.example.security.service.facade.EtudiantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class EtudiantServiceImpl implements EtudiantService {

    private final EtudiantRepository etudiantRepository;

    public EtudiantServiceImpl(EtudiantRepository etudiantRepository) {

        this.etudiantRepository = etudiantRepository;
    }


    @Override
    public int updateComplet(String email, String codeMassar, String codeCIN, LocalDate dateNaissance, String lieuNaissance, String adresse, String genre, String nationalite, String niveauScolaire, String mentionBac, String typeBac, String anneeBac) throws IOException {
        System.out.println("Début de updateComplet pour: " + email);
        Etudiant etudiant = etudiantRepository.findByEmail(email);
        if (etudiant == null) {
            System.out.println("Étudiant non trouvé: " + email);
            return -1;
        }
        if (codeMassar != null) etudiant.setCodeMassar(codeMassar);
        if (codeCIN != null) etudiant.setCodeCIN(codeCIN);
        if (dateNaissance != null) etudiant.setDateNaissance(dateNaissance);
        if (lieuNaissance != null) etudiant.setLieuNaissance(lieuNaissance);
        if (adresse != null) etudiant.setAdresse(adresse);
        if (genre != null) etudiant.setGenre(genre);
        if (nationalite != null) etudiant.setNationalite(nationalite);
        if (niveauScolaire != null) etudiant.setNiveauScolaire(niveauScolaire);
        if (mentionBac != null) etudiant.setMentionBac(mentionBac);
        if (anneeBac != null) etudiant.setAnneeBac(anneeBac);
        if (typeBac != null) etudiant.setTypeBac(typeBac);
        etudiantRepository.save(etudiant);
        System.out.println("Étudiant sauvegardé avec succès.");
        return 0;

    }

    @Override
    public Etudiant findByEmail(String email) {

        return etudiantRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public int deleteById(Long id) {
        if (!etudiantRepository.existsById(id)) {
            return -1; // ID non trouvé
        } else {
            etudiantRepository.deleteById(id);
            return 1; // suppression réussie
        }
    }

    @Override
    public List<Etudiant> findAll() {

        return etudiantRepository.findAll();
    }

    @Override
    public Etudiant getEtudiantById(Long id) {

        return etudiantRepository.findById(id).orElse(null);
    }


}