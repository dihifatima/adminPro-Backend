package com.example.security.service.impl;

import com.example.security.Authentification.security.JwtService;
import com.example.security.Authentification.user.User;
import com.example.security.Authentification.user.UserRepository;
import com.example.security.dao.DemandeServiceRepository;
import com.example.security.dao.ServiceOffertRepository;
import com.example.security.dao.CreneauRepository; // Ajout
import com.example.security.entity.Creneau;
import com.example.security.entity.DemandeService;
import com.example.security.entity.Etudiant;
import com.example.security.entity.ServiceOffert;
import com.example.security.service.facade.CreneauService;
import com.example.security.service.facade.DemandeServiceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DemandeServiceServiceImpl implements DemandeServiceService {

    @Autowired
    private final DemandeServiceRepository demandeServiceRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ServiceOffertRepository serviceOffertRepository;
    @Autowired
    private final CreneauService creneauService;
    @Autowired
    private final CreneauRepository creneauRepository; // Ajout
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final HttpServletRequest request;

    public DemandeServiceServiceImpl(DemandeServiceRepository demandeServiceRepository,
                                     UserRepository userRepository,
                                     ServiceOffertRepository serviceOffertRepository,
                                     CreneauService creneauService,
                                     CreneauRepository creneauRepository, // Ajout
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

        // Vérifier les conditions
        validateDemande(demande);

        if (hasDemandeForUserAndService(user.getEmail(), serviceOffert.getName())) {
            throw new RuntimeException("Une demande existe déjà pour ce service.");
        }

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
        newDemande.setRef(generateReference());
        newDemande.setDateRendezvous(demande.getDateRendezvous());
        newDemande.setDateSoumission(LocalDateTime.now());
        newDemande.setStatut(creneau != null ? "ACCEPTEE" : "EN_ATTENTE");
        newDemande.setUser(user);
        newDemande.setServiceOffert(serviceOffert);
        newDemande.setCreneau(creneau);

        return demandeServiceRepository.save(newDemande);
    }
    @Override
    public DemandeService save(DemandeService demande) {
        if (demande == null) {
            throw new RuntimeException("DemandeService ne peut pas être null");
        }

        // Extraire le token et récupérer l'utilisateur
        User user = getCurrentUser();

        // Déterminer le service offert
        ServiceOffert serviceOffert = determineServiceOffert(user);

        // Vérifier les conditions
        validateDemande(demande);

        if (hasDemandeForUserAndService(user.getEmail(), serviceOffert.getName())) {
            throw new RuntimeException("Une demande existe déjà pour ce service.");
        }

        // Construire la nouvelle demande
        DemandeService newDemande = new DemandeService();
        newDemande.setRef(generateReference());
        newDemande.setDateRendezvous(demande.getDateRendezvous());
        newDemande.setDateSoumission(LocalDateTime.now());
        newDemande.setStatut("EN_ATTENTE");
        newDemande.setUser(user);
        newDemande.setServiceOffert(serviceOffert);

        // **CORRECTION 1** : Chercher un créneau disponible correspondant à la date/heure demandée
        Creneau creneauDisponible = findAvailableCreneauForDateTime(demande.getDateRendezvous());
        if (creneauDisponible != null) {
            newDemande.setCreneau(creneauDisponible);
            // Optionnellement, vous pouvez réserver le créneau immédiatement
            // creneauService.reserverCreneau(creneauDisponible.getId());
        }

        return demandeServiceRepository.save(newDemande);
    }

    @Override
    public DemandeService update(DemandeService demande) {
        if (demande == null || demande.getId() == null) {
            throw new RuntimeException("DemandeService ou son ID ne peut pas être null");
        }

        DemandeService existing = findById(demande.getId());
        if (existing == null) {
            throw new RuntimeException("DemandeService non trouvée avec l'ID : " + demande.getId());
        }

        validateDemande(demande);

        // Mise à jour des champs modifiables
        existing.setDateRendezvous(demande.getDateRendezvous());
        existing.setStatut(demande.getStatut());

        // **CORRECTION 2** : Mettre à jour le créneau si la date/heure change
        if (demande.getCreneau() != null) {
            existing.setCreneau(demande.getCreneau());
        } else if (demande.getDateRendezvous() != null) {
            // Chercher un nouveau créneau correspondant
            Creneau nouveauCreneau = findAvailableCreneauForDateTime(demande.getDateRendezvous());
            if (nouveauCreneau != null) {
                existing.setCreneau(nouveauCreneau);
            }
        }

        return demandeServiceRepository.save(existing);
    }

    @Override
    public DemandeService accepterDemande(String ref, Long creneauId) {
        DemandeService demande = findByRef(ref);

        if (!"EN_ATTENTE".equals(demande.getStatut())) {
            throw new RuntimeException("Seules les demandes en attente peuvent être acceptées");
        }

        // **CORRECTION 3** : Vérifier la cohérence entre créneau et date de rendez-vous
        Creneau creneau = creneauService.findById(creneauId);
        if (!isCreneauCompatibleWithDate(creneau, demande.getDateRendezvous())) {
            throw new RuntimeException("Le créneau sélectionné ne correspond pas à la date de rendez-vous demandée");
        }

        // Vérifier et réserver le créneau
        if (!creneauService.isCreneauAvailable(creneauId)) {
            throw new RuntimeException("Le créneau sélectionné n'est pas disponible");
        }

        Creneau creneauReserve = creneauService.reserverCreneau(creneauId);

        demande.setStatut("ACCEPTEE");
        demande.setCreneau(creneauReserve);

        return demandeServiceRepository.save(demande);
    }

    // **NOUVELLE MÉTHODE** : Trouver un créneau disponible pour une date/heure donnée
    private Creneau findAvailableCreneauForDateTime(LocalDateTime dateRendezvous) {
        if (dateRendezvous == null) {
            return null;
        }

        LocalDate date = dateRendezvous.toLocalDate();
        LocalTime heure = dateRendezvous.toLocalTime();

        // Chercher les créneaux pour cette date
        List<Creneau> creneauxDuJour = creneauRepository.findByDateCreneauAndActifTrue(date);

        // Trouver le créneau qui englobe l'heure demandée
        for (Creneau creneau : creneauxDuJour) {
            if (creneau.getHeureDebut() != null && creneau.getHeureFin() != null) {
                if ((heure.equals(creneau.getHeureDebut()) || heure.isAfter(creneau.getHeureDebut())) &&
                        heure.isBefore(creneau.getHeureFin()) &&
                        creneau.getCapaciteRestante() > 0) {
                    return creneau;
                }
            }
        }

        return null;
    }

    // **NOUVELLE MÉTHODE** : Vérifier la compatibilité entre créneau et date
    private boolean isCreneauCompatibleWithDate(Creneau creneau, LocalDateTime dateRendezvous) {
        if (creneau == null || dateRendezvous == null) {
            return false;
        }

        LocalDate dateRdv = dateRendezvous.toLocalDate();
        LocalTime heureRdv = dateRendezvous.toLocalTime();

        // Vérifier que la date correspond
        if (!creneau.getDateCreneau().equals(dateRdv)) {
            return false;
        }

        // Vérifier que l'heure est dans la plage du créneau
        return (heureRdv.equals(creneau.getHeureDebut()) || heureRdv.isAfter(creneau.getHeureDebut())) &&
                heureRdv.isBefore(creneau.getHeureFin());
    }

    // **NOUVELLE MÉTHODE** : API pour obtenir les créneaux disponibles pour une date
    public List<Creneau> getAvailableCreneauxForDate(LocalDate date) {
        return creneauRepository.findByDateCreneauAndActifTrueAndCapaciteRestanteGreaterThan(date, 0);
    }

    // Reste des méthodes inchangées...
    @Override
    public DemandeService findById(Long id) {
        return demandeServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DemandeService non trouvée avec l'ID : " + id));
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
    public int deleteById(Long id) {
        DemandeService demande = findById(id);
        demandeServiceRepository.delete(demande);
        return 1;
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

    @Override
    public DemandeService refuserDemande(String ref, String motifRefus) {
        DemandeService demande = findByRef(ref);

        if (!"EN_ATTENTE".equals(demande.getStatut())) {
            throw new RuntimeException("Seules les demandes en attente peuvent être refusées");
        }

        demande.setStatut("REFUSEE");
        return demandeServiceRepository.save(demande);
    }

    @Override
    public DemandeService annulerDemande(String ref) {
        DemandeService demande = findByRef(ref);

        // Libérer le créneau si la demande était acceptée
        if ("ACCEPTEE".equals(demande.getStatut()) && demande.getCreneau() != null) {
            creneauService.libererCreneau(demande.getCreneau().getId());
        }

        demande.setStatut("ANNULEE");
        demande.setCreneau(null);

        return demandeServiceRepository.save(demande);
    }

    // Autres méthodes inchangées...
    @Override
    public List<DemandeService> findByUserFullName(String userNom) {
        return demandeServiceRepository.findByUserFullName(userNom);
    }

    @Override
    public List<DemandeService> findByServiceOffertNom(String serviceOffertNom) {
        return demandeServiceRepository.findByServiceOffert_Name(serviceOffertNom);
    }

    @Override
    public List<DemandeService> findByStatut(String statut) {
        return demandeServiceRepository.findByStatut(statut);
    }

    @Override
    public List<DemandeService> findByUser(Long userId) {
        return demandeServiceRepository.findByUserId(userId);
    }

    @Override
    public List<DemandeService> findByDateRange(LocalDate dateDebut, LocalDate dateFin) {
        LocalDateTime dateTimeDebut = dateDebut.atStartOfDay();
        LocalDateTime dateTimeFin = dateFin.atTime(23, 59, 59);
        return demandeServiceRepository.findByDateRendezvousBetween(dateTimeDebut, dateTimeFin);
    }

    @Override
    public List<DemandeService> findDemandesEnAttente() {
        return findByStatut("EN_ATTENTE");
    }

    @Override
    public List<DemandeService> findDemandesAcceptees() {
        return findByStatut("ACCEPTEE");
    }

    @Override
    public List<DemandeService> findDemandesRefusees() {
        return findByStatut("REFUSEE");
    }

    @Override
    public List<LocalDateTime> findAllReservedDates() {
        return demandeServiceRepository.findAllReservedDates();
    }

    @Override
    public List<DemandeService> findByDateRendezvous(LocalDate date) {
        LocalDateTime dateTimeDebut = date.atStartOfDay();
        LocalDateTime dateTimeFin = date.atTime(23, 59, 59);
        return demandeServiceRepository.findByDateRendezvousBetween(dateTimeDebut, dateTimeFin);
    }

    @Override
    public boolean isDateDisponible(LocalDateTime dateRendezvous) {
        List<LocalDateTime> reservedDates = findAllReservedDates();
        return !reservedDates.contains(dateRendezvous);
    }

    @Override
    public boolean hasDemandeForUserAndService(String userEmail, String serviceName) {
        DemandeService existing = demandeServiceRepository.findByUser_EmailAndServiceOffert_Name(userEmail, serviceName);
        return existing != null;
    }

    @Override
    public long countDemandesByStatut(String statut) {
        return demandeServiceRepository.countByStatut(statut);
    }

    @Override
    public long countDemandesByUser(Long userId) {
        return demandeServiceRepository.countByUserId(userId);
    }

    @Override
    public long countDemandesByService(Long serviceId) {
        return demandeServiceRepository.countByServiceOffertId(serviceId);
    }

    @Override
    public List<DemandeService> findRecentDemandes(int nombreJours) {
        LocalDateTime dateLimit = LocalDateTime.now().minusDays(nombreJours);
        return demandeServiceRepository.findByDateSoumissionAfter(dateLimit);
    }

    @Override
    public void validateDemande(DemandeService demande) {
        if (demande.getDateRendezvous() == null) {
            throw new RuntimeException("La date de rendez-vous est obligatoire");
        }

        if (demande.getDateRendezvous().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("La date de rendez-vous ne peut pas être dans le passé");
        }
    }

    @Override
    public boolean canUserCreateDemande(String userEmail, String serviceName) {
        return !hasDemandeForUserAndService(userEmail, serviceName);
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
                    .orElseThrow(() -> new RuntimeException("Service orientation académique introuvable"));
        } else {
            throw new RuntimeException("Type de client non supporté pour le moment");
        }
    }

    private String generateReference() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}