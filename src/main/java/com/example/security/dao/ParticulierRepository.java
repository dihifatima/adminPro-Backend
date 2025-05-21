package com.example.security.dao;

import com.example.security.entity.Etudiant;
import com.example.security.entity.Particulier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticulierRepository extends JpaRepository<Particulier, Long> {

    Particulier findByEmail(String email);
}