package com.example.security.dao;

import com.example.security.entity.Admin;
import com.example.security.entity.AdminSecondaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminSecondaireRepository extends JpaRepository<AdminSecondaire, Long> {
}


