package com.example.security.dao;
import com.example.security.entity.BlockActuality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlockActualityRepository extends JpaRepository<BlockActuality, Long> {


    // Charger un article avec ses sections
    @Query("SELECT a FROM BlockActuality a LEFT JOIN FETCH a.sections WHERE a.id = :id")
    Optional<BlockActuality> findByIdWithSections(@Param("id") Long id);

    // Charger tous les articles avec leurs sections
    @Query("SELECT DISTINCT a FROM BlockActuality a LEFT JOIN FETCH a.sections")
    List<BlockActuality> findAllWithSections();
}