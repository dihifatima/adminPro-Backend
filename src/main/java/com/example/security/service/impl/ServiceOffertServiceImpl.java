package com.example.security.service.impl;
import com.example.security.dao.ServiceOffertRepository;
import com.example.security.entity.ServiceOffert;
import com.example.security.service.facade.ServiceOffertService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class ServiceOffertServiceImpl implements ServiceOffertService {

    private final ServiceOffertRepository serviceOffertRepository;

    public ServiceOffertServiceImpl(ServiceOffertRepository serviceOffertRepository) {
        this.serviceOffertRepository = serviceOffertRepository;
    }


    @Override
    public Optional<ServiceOffert> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du service ne doit pas être vide.");
        }
        return serviceOffertRepository.findByName(name);
    }

    @Override
    @Transactional
    public ServiceOffert updateServiceDetails(Long id, String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nouveau nom du service ne peut pas être vide.");
        }

        Optional<ServiceOffert> existing = serviceOffertRepository.findByName(newName);
        if (existing.isPresent() && !existing.get().getId().equals(id)) {
            throw new IllegalArgumentException("Un autre service avec ce nom existe déjà.");
        }

        return serviceOffertRepository.findById(id).map(service -> {
            service.setName(newName);
            return serviceOffertRepository.save(service);
        }).orElseThrow(() -> new IllegalArgumentException("ServiceOffert non trouvé avec l'ID : " + id));
    }

    @Override
    public List<ServiceOffert> findAll() {
        //trier par createdAt
        return serviceOffertRepository.findAll(Sort.by(Sort.Direction.ASC, "createdAt"));
    }

    @Override
    public ServiceOffert save(ServiceOffert service) {
        if (service == null || service.getName() == null || service.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du service est obligatoire.");
        }

        Optional<ServiceOffert> existing = serviceOffertRepository.findByName(service.getName());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Ce nom de service est déjà utilisé.");
        }

        return serviceOffertRepository.save(service);
    }

    @Override
    public void deleteById(Long id) {
        serviceOffertRepository.deleteById(id);
    }
}