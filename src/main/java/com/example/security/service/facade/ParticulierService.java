package com.example.security.service.facade;
import com.example.security.entity.Particulier;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface ParticulierService {


    int updateComplet(String email,
                      String objetDemande,
                      String ville,
                      String adresse,
                      String codeCIN,
                      String lieuNaissance,
                      String nationalite,
                      String genre,
                      LocalDate dateNaissance) throws IOException;

    Particulier findByEmail(String email);
    int deleteById(Long id);
    List<Particulier> findAll();
    Particulier getParticulierById(Long id);

}

