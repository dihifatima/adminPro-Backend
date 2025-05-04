package com.example.security.dao;

import com.example.security.entity.Particulier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticulierRepository extends JpaRepository<Particulier, Long> {

    List<Particulier> findByFirstnameOrLastname(String firstname, String lastname);

    Particulier findByEmail(String email);
}