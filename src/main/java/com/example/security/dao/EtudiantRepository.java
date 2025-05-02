package com.example.security.dao;

import com.example.security.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Etudiant findByEmail(String email);
    List<Etudiant> findByFirstnameORLastname(String firstname, String lastname);

}
