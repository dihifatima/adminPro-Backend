package com.example.security.dao;

import com.example.security.entity.DemandeService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface DemandeServiceRepository extends JpaRepository<DemandeService, Long> {
    DemandeService findByRef(String ref);

    @Query("SELECT d FROM DemandeService d WHERE CONCAT(d.user.firstname, ' ', d.user.lastname) = :fullName")
    List<DemandeService> findByUserFullName(@Param("fullName") String fullName);

    List<DemandeService> findByServiceOffert_Name(String name);

    DemandeService findByUser_EmailAndServiceOffert_Name(String email, String name);
    // Dans DemandeServiceRepository.java
    // Méthode corrigée pour récupérer toutes les dates de rendez-vous
    @Query("SELECT d.dateRendezvous FROM DemandeService d WHERE d.dateRendezvous IS NOT NULL")
    List<LocalDateTime> findAllReservedDates();

}