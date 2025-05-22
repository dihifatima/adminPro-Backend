package com.example.security.dao;

import com.example.security.entity.DemandeurVisa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandeurVisaRepository extends JpaRepository<DemandeurVisa, Long> {
    DemandeurVisa findByEmail(String email);

}
