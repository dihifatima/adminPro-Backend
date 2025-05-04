package com.example.security.service.impl;

import com.example.security.Authentification.user.User;
import com.example.security.dao.DemandeServiceRepository;
import com.example.security.dao.NotificationRepository;
import com.example.security.dao.ServiceOffertRepository;
import com.example.security.entity.DemandeService;
import com.example.security.entity.EtatDemande;
import com.example.security.entity.Notification;
import com.example.security.service.facade.DemandeServiceService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DemandeServiceServiceImpl implements DemandeServiceService {

    private final DemandeServiceRepository demandeServiceRepository;
    private final ServiceOffertRepository serviceRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public DemandeServiceServiceImpl(
            DemandeServiceRepository demandeServiceRepository,
            ServiceOffertRepository serviceRepository,
            NotificationRepository notificationRepository) {
        this.demandeServiceRepository = demandeServiceRepository;
        this.serviceRepository = serviceRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional
    public DemandeService create(DemandeService demandeService) {
        // Vérifier que le service existe
        if (demandeService.getServiceOffert() == null ||
                !serviceRepository.existsById(demandeService.getServiceOffert().getIdService())) {
            return null;
        }

        // Définir l'état initial et la date de soumission
        demandeService.setEtat(EtatDemande.EN_ATTENTE);
        demandeService.setDateSoumission(LocalDateTime.now());

        // Sauvegarder la demande
        DemandeService savedDemande = demandeServiceRepository.save(demandeService);

        // Créer une notification pour les admins
        // Note: Dans un cas réel, il faudrait trouver les admins concernés
        // Pour simplifier, on suppose que la méthode notifierAdmins est implémentée ailleurs
        notifierAdmins(savedDemande);

        return savedDemande;
    }

    @Override
    @Transactional
    public DemandeService updateStatus(Long idDemande, EtatDemande nouvelEtat) {
        Optional<DemandeService> optionalDemande = demandeServiceRepository.findById(idDemande);
        if (optionalDemande.isEmpty()) {
            return null;
        }

        DemandeService demande = optionalDemande.get();
        demande.setEtat(nouvelEtat);

        // Notification à l'utilisateur du changement d'état
        notifierChangementEtat(demande);

        return demandeServiceRepository.save(demande);
    }

    @Override
    @Transactional
    public DemandeService planifierRDV(Long idDemande, LocalDateTime dateRDV) {
        Optional<DemandeService> optionalDemande = demandeServiceRepository.findById(idDemande);
        if (optionalDemande.isEmpty()) {
            return null;
        }

        DemandeService demande = optionalDemande.get();
        demande.setDateRDV(dateRDV);

        // Notification à l'utilisateur du RDV planifié
        notifierRDVPlanifie(demande);

        return demandeServiceRepository.save(demande);
    }

    @Override
    public DemandeService findById(Long id) {
        return demandeServiceRepository.findById(id).orElse(null);
    }

    @Override
    public List<DemandeService> findAll() {
        return demandeServiceRepository.findAll();
    }

    @Override
    public List<DemandeService> findByUser(User user) {
        return demandeServiceRepository.findByClient(user);
    }

    @Override
    public List<DemandeService> findByEtat(EtatDemande etat) {
        return demandeServiceRepository.findByEtat(etat);
    }

    @Override
    public List<DemandeService> findByDateSoumissionBetween(LocalDateTime debut, LocalDateTime fin) {
        return demandeServiceRepository.findByDateSoumissionBetween(debut, fin);
    }

    @Override
    public List<DemandeService> findRecentDemandes() {
        return demandeServiceRepository.findRecentDemandes();
    }

    @Override
    @Transactional
    public int delete(Long id) {
        if (!demandeServiceRepository.existsById(id)) {
            return -1; // Demande non trouvée
        }
        demandeServiceRepository.deleteById(id);
        return 1; // Suppression réussie
    }

    @Override
    public Long countByEtat(EtatDemande etat) {
        return demandeServiceRepository.countByEtat(etat);
    }

    // Méthodes privées pour gérer les notifications

    private void notifierAdmins(DemandeService demande) {
        // Dans une implémentation réelle, vous récupéreriez les admins concernés
        // Pour l'exemple, créons une notification générique
        Notification notification = new Notification();
        notification.setDemandeService(demande);
        notification.setSujet("Nouvelle demande de service");
        notification.setMessage("Une nouvelle demande pour le service " +
                demande.getServiceOffert().getNom() + " a été soumise par " +
                demande.getClient().getFirstname() + " " +
                demande.getClient().getLastname());
        notification.setDateCreation(LocalDateTime.now());
        notification.setVue(false);

        // Dans un cas réel, il faudrait trouver les admins par département
        // et créer une notification pour chacun
        // Pour l'exemple, nous supposons que l'admin sera défini plus tard

        notificationRepository.save(notification);
    }

    private void notifierChangementEtat(DemandeService demande) {
        Notification notification = new Notification();
        notification.setDestinataire(demande.getClient());
        notification.setDemandeService(demande);
        notification.setSujet("Mise à jour de votre demande");
        notification.setMessage("Votre demande pour le service " +
                demande.getServiceOffert().getNom() +
                " est maintenant " + demande.getEtat().name());
        notification.setDateCreation(LocalDateTime.now());
        notification.setVue(false);

        notificationRepository.save(notification);
    }

    private void notifierRDVPlanifie(DemandeService demande) {
        Notification notification = new Notification();
        notification.setDestinataire(demande.getClient());
        notification.setDemandeService(demande);
        notification.setSujet("Rendez-vous planifié");
        notification.setMessage("Un rendez-vous a été planifié pour votre demande de " +
                demande.getServiceOffert().getNom() + " le " +
                demande.getDateRDV());
        notification.setDateCreation(LocalDateTime.now());
        notification.setVue(false);

        notificationRepository.save(notification);
    }
}
