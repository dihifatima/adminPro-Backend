package com.example.security.service.facade;

import com.example.security.entity.Etudiant;

import java.util.List;

public interface EtudiantService {
    int save(Etudiant etudiant);
    /*
     le type de routeur est un int pour : pour indiquer un code de résultat

       public int save(Etudiant etudiant) {
    if (etudiantRepository.findByEmail(etudiant.getEmail()) != null) {
        return -1; // étudiant déjà existant
    } else {
        etudiantRepository.save(etudiant);
        return 1; // succès
    }
}
*/
    int update(Etudiant etudiant);
    Etudiant findByEmail(String email);
    int deleteById(Long id);
    List<Etudiant> findAll();
}
