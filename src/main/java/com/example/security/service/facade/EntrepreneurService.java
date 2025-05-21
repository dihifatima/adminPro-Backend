package com.example.security.service.facade;

import com.example.security.entity.Entrepreneur;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
@Service
public interface EntrepreneurService {
    int updateComplet(String email,
                      String cin,
                      String nomEntreprise,
                      String secteurActivite,
                      String registreCommerce,
                      String identifiantFiscal,
                      String typeEntreprise,
                      LocalDate dateCreation,
                      String siegeSocial) throws IOException;

    Entrepreneur findByEmail(String email);
    int deleteById(Long id);
    List< Entrepreneur> findAll();
    Entrepreneur getEntrepreneurById(Long id);

}

