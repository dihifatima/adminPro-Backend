package com.example.security.service.impl;




import com.example.security.Authentification.user.User;
import com.example.security.Authentification.user.UserRepository;
import com.example.security.dao.DemandeServiceRepository;
import com.example.security.dao.ServiceOffertRepository;
import com.example.security.entity.DemandeService;
import com.example.security.entity.ServiceOffert;
import com.example.security.service.facade.DemandeServiceService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class DemandeServiceServiceImpl implements DemandeServiceService {

    private final DemandeServiceRepository demandeServiceRepository;

    private final UserRepository userRepository;

    private final ServiceOffertRepository serviceOffertRepository;



    public DemandeServiceServiceImpl(DemandeServiceRepository demandeServiceRepository, UserRepository userRepository, ServiceOffertRepository serviceOffertRepository) {
        this.demandeServiceRepository = demandeServiceRepository;
        this.userRepository = userRepository;
        this.serviceOffertRepository = serviceOffertRepository;
    }



    @Override
    public DemandeService save(DemandeService demandeService,Long serviceOffertId, Long userId ) {

        LocalDateTime dateRendezvous = demandeService.getDateRendezvous();
        String statut = demandeService.getStatut();

        Optional<User> userOpt = userRepository.findById(userId);
        String userNom = userOpt.get().getFullName();

        Optional<ServiceOffert> serviceOffertOpt = serviceOffertRepository.findById(serviceOffertId);
        String serviceOffertNom = serviceOffertOpt.get().getName();

        if (userOpt.isEmpty() || serviceOffertOpt.isEmpty()) {
            return null;
        }


        demandeService.setUser(userOpt.get());
        demandeService.setServiceOffert(serviceOffertOpt.get());
        demandeService.setUserNom(userNom);
        demandeService.setServiceOffertNom(serviceOffertNom);


        demandeService.setDateSoumission(LocalDateTime.now());
        demandeService.setDateRendezvous(dateRendezvous);
        demandeService.setStatut(statut);

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