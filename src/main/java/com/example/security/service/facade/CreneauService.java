package com.example.security.service.facade;
import com.example.security.entity.Creneau;
import java.util.List;

 public interface CreneauService {

     Creneau findById(Long id);
    List<Creneau> findAll();
     boolean isCreneauAvailable(Long creneauId);
     Creneau reserverCreneau(Long creneauId);
     Creneau updateCreneauStatus(Long creneauId, Boolean actif);
     List<Creneau> findAvailableCreneauxForBooking();

 }