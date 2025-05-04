package com.example.security.service.facade;

import com.example.security.entity.ServiceOffert;
import com.example.security.entity.TypeService;
import com.example.security.ws.dto.ServiceOffertDto;

import java.util.List;

public interface ServiceOffertService {
    // Opérations CRUD de base
    ServiceOffert save(ServiceOffert service);

    // Cette méthode permet d’enregistrer un nouveau service offert dans la base de données.
    ServiceOffert update(ServiceOffert service);

    //➤ Elle permet de modifier un service existant, en mettant à jour ses informations.
    int delete(Long id);

    //➤ Supprime un service par son identifiant (id).
//➤ Retourne un entier (souvent 1 si la suppression réussit, 0 sinon).
    // Recherche
    ServiceOffert findById(Long id);

    //➤ Recherche et retourne un service offert à partir de son identifiant unique.
    List<ServiceOffert> findAll();

    //➤ Retourne la liste de tous les services offerts enregistrés.
    List<ServiceOffert> findByType(TypeService type);

    //➤ Retourne la liste des services ayant un type spécifique (ex. : Visa, Éducation...).
    List<ServiceOffert> findByEstDisponible(boolean estDisponible);

    //Filtre les services disponibles ou non, selon la valeur du paramètre (true ou false).
    // Opérations avancées
    List<ServiceOffert> findByNomContainingIgnoreCase(String nomPartiel);

    //Recherche des services dont le nom contient une certaine chaîne de caractères, sans tenir compte de la casse (majuscule/minuscule).
    List<ServiceOffert> findByTypeAndEstDisponibleTrue(TypeService type);
//Retourne les services disponibles qui appartiennent à un type donné.
}
