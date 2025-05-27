package com.example.security.service.facade;
import com.example.security.entity.Creneau;
import java.util.List;

 public interface CreneauService {

    Creneau save(Creneau creneau);             // Créer ou modifier un créneau

    Creneau findById(Long id);                  // Chercher un créneau par son id

    List<Creneau> findAll();                    // Lister tous les créneaux

    boolean isCreneauAvailable(Long creneauId);// Vérifier disponibilité d’un créneau

    Creneau reserverCreneau(Long creneauId);   // Réserver un créneau

    Creneau libererCreneau(Long creneauId);    // Libérer un créneau réservé
}