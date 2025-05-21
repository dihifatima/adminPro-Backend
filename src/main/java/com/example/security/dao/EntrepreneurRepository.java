package com.example.security.dao;

import com.example.security.entity.Entrepreneur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntrepreneurRepository extends JpaRepository<Entrepreneur, Long> {

    Entrepreneur findByEmail(String email);
}
