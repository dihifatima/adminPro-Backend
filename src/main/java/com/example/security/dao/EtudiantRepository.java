package com.example.security.dao;

import com.example.security.entity.Etudiant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    List<Etudiant> findByFirstnameOrLastname(String firstname, String lastname);

    Etudiant findByEmail(String email);


}
