package com.example.security.service.impl;

import com.example.security.dao.EtudiantRepository;
import com.example.security.entity.Etudiant;
import com.example.security.service.facade.EtudiantService;
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
        Etudiant oldEtudiant = findByEmail(etudiant.getEmail());
        if (oldEtudiant == null) {
            return -1;
        } else {
       /*
-Cette méthode permet de mettre à jour les informations d'un étudiant.
-Elle vérifie d'abord si l'étudiant existe dans la base de données avec findByEmail.
Si l'étudiant n'existe pas, elle retourne -1.
Si l'étudiant existe, l'ID de l'étudiant est mis à jour avec l'ID de l'ancien étudiant
avant de sauvegarder les nouvelles informations.
 La méthode retourne 0 après la mise à jour.
          */
            etudiant.setId(oldEtudiant.getId());
            etudiantRepository.save(etudiant);
            return 0;
        }
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

}



