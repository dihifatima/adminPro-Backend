package com.example.security.service.facade;
import com.example.security.entity.Creneau;
import java.util.List;

 public interface CreneauService {



     Creneau findById(Long id);                  // Chercher un créneau par son id
    List<Creneau> findAll();                    // Lister tous les créneaux
    boolean isCreneauAvailable(Long creneauId);// Vérifier disponibilité d’un créneau

     Creneau reserverCreneau(Long creneauId);



     Creneau updateCreneauStatus(Long creneauId, Boolean actif);

     /**
      * Récupère seulement les créneaux actifs et disponibles
      */
     List<Creneau> findAvailableCreneauxForBooking();

 }