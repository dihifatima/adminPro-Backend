package com.example.security.dao;

import com.example.security.entity.ServiceOffert;
import com.example.security.entity.TypeService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceOffertRepository extends JpaRepository<ServiceOffert, Long> {
    List<ServiceOffert> findByEstDisponible(boolean estDisponible);
    List<ServiceOffert> findByType(TypeService type);

    // Recherche par nom contenant une sous-chaîne (insensible à la casse)
    List<ServiceOffert> findByNomContainingIgnoreCase(String nomPartiel);

    // Recherche des services disponibles d'un type spécifique
    @Query("SELECT s FROM ServiceOffert s WHERE s.type = :type AND s.estDisponible = true")
    List<ServiceOffert> findByTypeAndEstDisponibleTrue(@Param("type") TypeService type);
}
