package com.example.security.service.facade;

import com.example.security.entity.DemandeurVisa;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
public interface DemandeurVisaService {

    DemandeurVisa findByEmail(String email);
    int deleteById(Long id);
    List<DemandeurVisa> findAll();
    DemandeurVisa getDemandeurVisaById(Long id);
    int updateComplet(String email, String codeCIN, LocalDate dateNaissance, String passportNumber, String lieuNaissance, String adresse, String genre, String nationalite, String destinationVisa, String typeVisa, String dureeSejour, LocalDate dateDelivrancePassport, LocalDate dateExpirationPassport) throws IOException;
}

