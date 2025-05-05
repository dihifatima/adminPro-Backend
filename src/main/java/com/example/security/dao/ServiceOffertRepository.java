package com.example.security.dao;

import com.example.security.entity.ServiceOffert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ServiceOffertRepository extends JpaRepository<ServiceOffert, Long> {

    Optional<ServiceOffert> findByName(String name);


}
