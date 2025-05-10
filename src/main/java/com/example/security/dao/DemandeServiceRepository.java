package com.example.security.dao;

import com.example.security.entity.DemandeService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface DemandeServiceRepository extends JpaRepository<DemandeService, Long> {

    DemandeService findByRef(String ref);

    List<DemandeService> findByUserNom(String userNom);
    List<DemandeService> findByServiceOffertNom(String serviceOffertNom);
    DemandeService findByUser_EmailAndServiceOffert_Name(String email, String name);


}
