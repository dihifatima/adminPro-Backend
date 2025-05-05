package com.example.security.dao;

import com.example.security.Authentification.role.Role;
import com.example.security.Authentification.user.User;
import com.example.security.entity.DemandeService;
import com.example.security.entity.ServiceOffert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ServiceOffertRepository extends JpaRepository<ServiceOffert, Long> {

    Optional<ServiceOffert> findByName(String name);
    void deleteById(Long id);


}
