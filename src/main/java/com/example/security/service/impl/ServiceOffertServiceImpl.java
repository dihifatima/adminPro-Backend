package com.example.security.service.impl;
import com.example.security.dao.ServiceOffertRepository;
import com.example.security.entity.ServiceOffert;
import com.example.security.service.facade.ServiceOffertService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class ServiceOffertServiceImpl implements ServiceOffertService {

    private final ServiceOffertRepository serviceOffertRepository;

    @Autowired
    public ServiceOffertServiceImpl(ServiceOffertRepository serviceOffertRepository) {
        this.serviceOffertRepository = serviceOffertRepository;
    }


    @Override
    public Optional<ServiceOffert> findByName(String name) {
        return serviceOffertRepository.findByName(name);
    }

    @Override
    @Transactional
    public ServiceOffert updateServiceDetails(Long id, String newName) {
        return serviceOffertRepository.findById(id).map(service -> {
            service.setName(newName);
            return serviceOffertRepository.save(service);
        }).orElseThrow(() -> new IllegalArgumentException("ServiceOffert not found for id: " + id));
    }

    @Override
    public List<ServiceOffert> findAll() {
        return serviceOffertRepository.findAll();
    }

    @Override
    public ServiceOffert save(ServiceOffert service) {
        return serviceOffertRepository.save(service);
    }
}