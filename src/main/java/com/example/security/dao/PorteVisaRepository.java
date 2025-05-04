package com.example.security.dao;

import com.example.security.entity.PorteVisa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PorteVisaRepository extends JpaRepository<PorteVisa, Long> {

    List<PorteVisa> findByFirstnameOrLastname(String firstname, String lastname);

    PorteVisa findByEmail(String email);
}
