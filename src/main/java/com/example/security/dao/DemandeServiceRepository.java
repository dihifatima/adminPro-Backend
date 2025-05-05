package com.example.security.dao;

import com.example.security.entity.DemandeService;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DemandeServiceRepository extends JpaRepository<DemandeService, Long> {

    List<DemandeService> findByServiceOffertId(Long serviceOffertId);

    List<DemandeService> findByUserId(Long userId);
}
