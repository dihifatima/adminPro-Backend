package com.example.security.service.facade;

import com.example.security.entity.ServiceOffert;

import java.util.List;
import java.util.Optional;

public interface ServiceOffertService {
    Optional<ServiceOffert> findByName(String name);
    ServiceOffert updateServiceDetails(Long id, String newName);
    List<ServiceOffert> findAll(); // إذا كنت تحتاجها في الواجهة
    ServiceOffert save(ServiceOffert service);}
