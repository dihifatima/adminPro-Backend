package com.example.security.service.facade;

import com.example.security.entity.PorteVisa;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface PorteVisaService   {


    PorteVisa findByEmail(String email);
    int deleteById(Long id);
    List<PorteVisa> findAll();
    PorteVisa getPorteVisaById(Long id);

    int updateComplet(String email, String codeCIN, LocalDate dateNaissance, String passportNumber, String lieuNaissance, String adresse, String genre, String nationalite, String destinationVisa, String typeVisa, String dureeSejour, LocalDate dateDelivrancePassport, LocalDate dateExpirationPassport) throws IOException;
}

