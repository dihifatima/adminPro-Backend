package com.example.security.service.impl;

import com.example.security.dao.ServiceOffertRepository;
import com.example.security.entity.ServiceOffert;
import com.example.security.entity.TypeService;
import com.example.security.service.facade.ServiceOffertService;
import com.example.security.ws.dto.ServiceOffertDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
@org.springframework.stereotype.Service
public class ServiceOffertServiceImpl implements ServiceOffertService {

    private final ServiceOffertRepository serviceOffertRepository;

    @Autowired
    public ServiceOffertServiceImpl(ServiceOffertRepository serviceRepository) {
        this.serviceOffertRepository = serviceRepository;
    }

    @Override
    public ServiceOffert save(ServiceOffert service) {

        return serviceOffertRepository.save(service);
    }

    @Override
    public ServiceOffert update(ServiceOffert service) {
        if (service.getIdService() == null || !serviceOffertRepository.existsById(service.getIdService())) {
            return null; // Ne peut pas mettre à jour un service qui n'existe pas
        }
        return serviceOffertRepository.save(service);
    }

    @Override
    @Transactional
    public int delete(Long id) {
        if (!serviceOffertRepository.existsById(id)) {
            return -1; // Service non trouvé
        }
        serviceOffertRepository.deleteById(id);
        return 1; // Suppression réussie
    }

    @Override
    public ServiceOffert findById(Long id) {

        return serviceOffertRepository.findById(id).orElse(null);
    }

    @Override
    public List<ServiceOffert> findAll() {

        return serviceOffertRepository.findAll();
    }

    @Override
    public List<ServiceOffert> findByType(TypeService type) {

        return serviceOffertRepository.findByType(type);
    }

    @Override
    public List<ServiceOffert> findByEstDisponible(boolean estDisponible) {
        return serviceOffertRepository.findByEstDisponible(estDisponible);
    }

    @Override
    public List<ServiceOffert> findByNomContainingIgnoreCase(String nomPartiel) {
        return serviceOffertRepository.findByNomContainingIgnoreCase(nomPartiel);
    }

    @Override
    public List<ServiceOffert> findByTypeAndEstDisponibleTrue(TypeService type) {
        return serviceOffertRepository.findByTypeAndEstDisponibleTrue(type);
    }




}
