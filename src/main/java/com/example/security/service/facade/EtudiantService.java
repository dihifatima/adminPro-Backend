package com.example.security.service.facade;

import com.example.security.entity.Etudiant;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface EtudiantService {
    int updateComplet(String email,
                      String codeMassar,
                      String codeCIN,
                      LocalDate dateNaissance,
                      String lieuNaissance,
                      String adresse,
                      String genre,
                      String nationalite,
                      String niveauScolaire,
                      String mentionBac,
                      String typeBac,
                      String anneeBac) throws IOException;

    Etudiant findByEmail(String email);
    int deleteById(Long id);
    List<Etudiant> findAll();//Récupérer tous les étudiants.
    Etudiant getEtudiantById(Long id); //Récupérer un étudiant par ID.


}
