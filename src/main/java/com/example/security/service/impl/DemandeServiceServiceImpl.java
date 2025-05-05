package com.example.security.service.impl;




import com.example.security.Authentification.user.User;
import com.example.security.Authentification.user.UserRepository;
import com.example.security.dao.DemandeServiceRepository;
import com.example.security.dao.ServiceOffertRepository;
import com.example.security.entity.DemandeService;
import com.example.security.entity.ServiceOffert;
import com.example.security.service.facade.DemandeServiceService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class DemandeServiceServiceImpl implements DemandeServiceService {
    @Autowired
    private final DemandeServiceRepository demandeServiceRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ServiceOffertRepository serviceOffertRepository;



    public DemandeServiceServiceImpl(DemandeServiceRepository demandeServiceRepository, UserRepository userRepository, ServiceOffertRepository serviceOffertRepository) {
        this.demandeServiceRepository = demandeServiceRepository;
        this.userRepository = userRepository;
        this.serviceOffertRepository = serviceOffertRepository;
    }



    @Override
    public DemandeService save(DemandeService demandeService, Long serviceOffertId, Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<ServiceOffert> serviceOffertOpt = serviceOffertRepository.findById(serviceOffertId);

        if (userOpt.isEmpty() || serviceOffertOpt.isEmpty()) {
            // Si l'utilisateur ou le service offert n'existe pas, retour d'une erreur ou null
            return null;
        }

        // Associer l'utilisateur et le service offert à la demande
        demandeService.setUser(userOpt.get());
        demandeService.setServiceOffert(serviceOffertOpt.get());

        // Sauvegarder la demande de service
        return demandeServiceRepository.save(demandeService);
    }


    @Override
    public Optional<DemandeService> findById(Long id) {
        return demandeServiceRepository.findById(id);
    }

    @Override
    public List<DemandeService> findAll() {
        return demandeServiceRepository.findAll();
    }

    @Override
    @Transactional
    public DemandeService updateStatut(Long id, String nouveauStatut) {
        return demandeServiceRepository.findById(id).map(demande -> {
            demande.setStatut(nouveauStatut);
            return demandeServiceRepository.save(demande);
        }).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        demandeServiceRepository.deleteById(id);
    }

    @Override
    public List<DemandeService> findByUserId(Long userId) {
        return demandeServiceRepository.findByUserId(userId);
    }

    @Override
    public List<DemandeService> findByServiceOffertId(Long serviceOffertId) {
        return demandeServiceRepository.findByServiceOffertId(serviceOffertId);
    }
}