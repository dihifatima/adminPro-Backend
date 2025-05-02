package com.example.security.dao;

import com.example.security.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    List<Etudiant> findByFirstnameOrLastname(String firstname, String lastname);

    Etudiant findByEmail(String email);

   /* @Query("SELECT e FROM Etudiant e JOIN User u ON e.id = u.id WHERE u.email = :email")
        Etudiant findByEmail(@Param("email") String email);
*/
    
}
