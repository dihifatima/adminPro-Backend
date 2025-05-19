package com.example.security.dao;

import com.example.security.entity.BlockActuality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockActualityRepository extends JpaRepository<BlockActuality, Long> {

    // Recherche des articles les plus vus
    List<BlockActuality> findTop5ByOrderByViewCountDesc();

}
