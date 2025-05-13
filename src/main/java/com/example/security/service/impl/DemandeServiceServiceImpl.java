package com.example.security.service.impl;

import com.example.security.Authentification.security.JwtService;
import com.example.security.Authentification.user.User;
import com.example.security.Authentification.user.UserRepository;
import com.example.security.dao.DemandeServiceRepository;
import com.example.security.dao.ServiceOffertRepository;
import com.example.security.entity.DemandeService;
import com.example.security.entity.Etudiant;
import com.example.security.entity.ServiceOffert;
import com.example.security.service.facade.DemandeServiceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DemandeServiceServiceImpl implements DemandeServiceService {

    private final DemandeServiceRepository demandeServiceRepository;
    private final UserRepository userRepository;
    private final ServiceOffertRepository serviceOffertRepository;
    private final JwtService jwtService;
    private final HttpServletRequest request;

    public DemandeServiceServiceImpl(DemandeServiceRepository demandeServiceRepository, UserRepository userRepository, ServiceOffertRepository serviceOffertRepository, JwtService jwtService, HttpServletRequest request) {
        this.demandeServiceRepository = demandeServiceRepository;
        this.userRepository = userRepository;
        this.serviceOffertRepository = serviceOffertRepository;
        this.jwtService = jwtService;
        this.request = request;
    }

    @Override
    public DemandeService save(DemandeService demande) {
        // 1. Extraire le token depuis l'en-tête Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token non fourni");
        }

        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwt);

        // 2. Récupérer l'utilisateur connecté
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // 3. Déterminer le service offert (exemple pour Étudiant)
        ServiceOffert serviceOffert;
        if (user instanceof Etudiant) {
            serviceOffert = serviceOffertRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Service orientation académique introuvable"));
        } else {
            throw new RuntimeException("Type de client non supporté pour le moment");
        }

        // ✅ 4. Vérifier s'il existe déjà une demande pour ce user + ce service
        DemandeService existing = demandeServiceRepository.findByUser_EmailAndServiceOffert_Name(
                user.getEmail(), serviceOffert.getName());

        if (existing != null) {
            throw new RuntimeException("Une demande existe déjà pour ce service.");
        }

        // 5. Construire une nouvelle instance de DemandeService
        DemandeService newDemande = new DemandeService();
        newDemande.setRef(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        newDemande.setDateRendezvous(demande.getDateRendezvous());
        newDemande.setDateSoumission(LocalDateTime.now());
        newDemande.setStatut("EN_ATTENTE");
        newDemande.setUser(user);
        newDemande.setServiceOffert(serviceOffert);

        // 6. Sauvegarde
        return demandeServiceRepository.save(newDemande);
    }

    @Override
    public DemandeService updateStatut(String ref, String nouveauStatut) {
        // Chercher la demande par la référence (ref)
        DemandeService demande = demandeServiceRepository.findByRef(ref);
        if (demande != null) {
            // Mettre à jour le statut de la demande
            demande.setStatut(nouveauStatut);
            return demandeServiceRepository.save(demande); // Sauvegarder la demande mise à jour
        } else {
            throw new RuntimeException("Demande non trouvée pour la référence : " + ref);
        }
    }

    @Override
    public DemandeService findByRef(String ref) {
        // Chercher la demande par la référence (ref)
        return demandeServiceRepository.findByRef(ref);
    }

    @Override
    public int deleteByRef(String ref) {
        // Vérifier si la demande existe
        DemandeService demande = demandeServiceRepository.findByRef(ref);
        if (demande != null) {
            // Supprimer la demande et retourner le nombre d'éléments supprimés
            demandeServiceRepository.delete(demande);
            return 1; // 1 élément supprimé
        } else {
            throw new RuntimeException("Demande non trouvée pour la référence : " + ref);
        }
    }

    @Override
    public List<DemandeService> findAll() {
        // Retourner toutes les demandes
        return demandeServiceRepository.findAll();
    }

    @Override
    public List<DemandeService> findByUserFullName(String userNom) {
        return demandeServiceRepository.findByUserFullName(userNom);
    }

    @Override
    public List<DemandeService> findByServiceOffertNom(String serviceOffertNom) {
        // Updated to use the corrected repository method
        return demandeServiceRepository.findByServiceOffert_Name(serviceOffertNom);
    }
}