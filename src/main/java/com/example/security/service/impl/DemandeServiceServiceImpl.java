package com.example.security.service.impl;
import com.example.security.Authentification.security.JwtService;
import com.example.security.Authentification.user.User;
import com.example.security.Authentification.user.UserRepository;
import com.example.security.dao.DemandeServiceRepository;
import com.example.security.dao.ServiceOffertRepository;
import com.example.security.dao.CreneauxRepository; // Ajout
import com.example.security.entity.*;
import com.example.security.service.facade.CreneauService;
import com.example.security.service.facade.DemandeServiceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class DemandeServiceServiceImpl implements DemandeServiceService {


    private final DemandeServiceRepository demandeServiceRepository;
    private final UserRepository userRepository;
    private final ServiceOffertRepository serviceOffertRepository;
    private final CreneauService creneauService;
    private final CreneauxRepository creneauRepository; // Ajout
    private final JwtService jwtService;
    private final HttpServletRequest request;

    public DemandeServiceServiceImpl(DemandeServiceRepository demandeServiceRepository,
                                     UserRepository userRepository,
                                     ServiceOffertRepository serviceOffertRepository,
                                     CreneauService creneauService,
                                     CreneauxRepository creneauRepository, // Ajout
                                     JwtService jwtService,
                                     HttpServletRequest request) {
        this.demandeServiceRepository = demandeServiceRepository;
        this.userRepository = userRepository;
        this.serviceOffertRepository = serviceOffertRepository;
        this.creneauService = creneauService;
        this.creneauRepository = creneauRepository; // Ajout
        this.jwtService = jwtService;
        this.request = request;
    }
    @Override
    public DemandeService saveAvecCreneau(DemandeService demande, Long creneauId) {
        if (demande == null) {
            throw new RuntimeException("DemandeService ne peut pas être null");
        }

        // Extraire le token et récupérer l'utilisateur
        User user = getCurrentUser();

        // Déterminer le service offert
        ServiceOffert serviceOffert = determineServiceOffert(user);
        // Vérifier et réserver le créneau si spécifié
        Creneau creneau = null;
        if (creneauId != null) {
            if (!creneauService.isCreneauAvailable(creneauId)) {
                throw new RuntimeException("Le créneau sélectionné n'est pas disponible");
            }
            creneau = creneauService.reserverCreneau(creneauId);
        }

        // Construire la nouvelle demande
        DemandeService newDemande = new DemandeService();
        newDemande.setRef(generateReference(user));
        newDemande.setDateSoumission(LocalDateTime.now());
        newDemande.setStatut("EN_ATTENTE");
        newDemande.setUser(user);
        newDemande.setServiceOffert(serviceOffert);
        newDemande.setCreneau(creneau);

        return demandeServiceRepository.save(newDemande);
    }


    @Override
    public DemandeService update(DemandeService demande) {
        if (demande == null || demande.getId() == null) {
            throw new RuntimeException("DemandeService ou son ID ne peut pas être null");
        }
        DemandeService existing = findByRef(demande.getRef());
        // Mise à jour des champs modifiables
        existing.setCreneau(demande.getCreneau());
        existing.setStatut(demande.getStatut());

        // **CORRECTION 2** : Mettre à jour le créneau si la date/heure change
        if (demande.getCreneau() != null) {
            existing.setCreneau(demande.getCreneau());
        }

        return demandeServiceRepository.save(existing);
    }

    @Override
    public DemandeService findByRef(String ref) {
        DemandeService demande = demandeServiceRepository.findByRef(ref);
        if (demande == null) {
            throw new RuntimeException("DemandeService non trouvée avec la référence : " + ref);
        }
        return demande;
    }

    @Override
    public List<DemandeService> findAll() {
        return demandeServiceRepository.findAll();
    }
    @Override
    public int deleteByRef(String ref) {
        DemandeService demande = findByRef(ref);
        demandeServiceRepository.delete(demande);
        return 1;
    }
    @Override
    public DemandeService updateStatut(String ref, String nouveauStatut) {
        DemandeService demande = findByRef(ref);
        demande.setStatut(nouveauStatut);
        return demandeServiceRepository.save(demande);
    }


    // Méthodes privées utilitaires inchangées...
    private User getCurrentUser() {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token non fourni");
        }

        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwt);

        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    private ServiceOffert determineServiceOffert(User user) {
        if (user instanceof Etudiant) {
            return serviceOffertRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Service pour étudiant introuvable"));
        } else if (user instanceof Entrepreneur) {
            return serviceOffertRepository.findById(2L)
                    .orElseThrow(() -> new RuntimeException("Service pour entrepreneur introuvable"));
        } else if (user instanceof DemandeurVisa) {
            return serviceOffertRepository.findById(3L)
                    .orElseThrow(() -> new RuntimeException("Service pour demandeur de visa introuvable"));
        } else if (user instanceof Particulier) {
            return serviceOffertRepository.findById(4L)
                    .orElseThrow(() -> new RuntimeException("Service pour particulier introuvable"));
        } else {
            throw new RuntimeException("Type de client non supporté");
        }
    }



    private String generateReference(User user) {
        // Obtenir la date actuelle
        LocalDate currentDate = LocalDate.now();
        String datePart = currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // Obtenir le type abrégé du client
        String clientType;
        if (user instanceof Etudiant) {
            clientType = "ETU";
        } else if (user instanceof Entrepreneur) {
            clientType = "ENT";
        } else if (user instanceof Particulier) {
            clientType = "PAR";
        } else if (user instanceof DemandeurVisa) {
            clientType = "POR";
        } else {
            throw new RuntimeException("Type de client non supporté");
        }

        // Obtenir l'ID du client (assure-toi que l'ID n'est pas null)
        Long id = user.getId();
        if (id == null) {
            throw new RuntimeException("ID du client introuvable");
        }

        String idFormatted = String.format("%04d", id); // ex: 0153

        // Construire la référence
        return datePart + "-" + clientType + idFormatted;
    }
}
