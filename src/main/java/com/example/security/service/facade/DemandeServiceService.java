package com.example.security.service.facade;
import com.example.security.entity.DemandeService;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

public interface DemandeServiceService {
    DemandeService saveAvecCreneau(DemandeService demande, Long creneauId);
    DemandeService update(DemandeService demande);
    DemandeService findByRef(String ref);
    List<DemandeService> findAll();
    int deleteByRef(String ref);
    DemandeService updateStatut(String ref, String nouveauStatut);


}