package com.example.security.dao;
import com.example.security.entity.DemandeService;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface DemandeServiceRepository extends JpaRepository<DemandeService, Long> {
    DemandeService findByRef(String ref);

}