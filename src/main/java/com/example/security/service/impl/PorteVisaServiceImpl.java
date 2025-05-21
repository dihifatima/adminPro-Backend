package com.example.security.service.impl;

import com.example.security.dao.PorteVisaRepository;
import com.example.security.entity.PorteVisa;
import com.example.security.service.facade.PorteVisaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
@Service
public class PorteVisaServiceImpl implements PorteVisaService {
    @Autowired
    private final PorteVisaRepository porteVisaRepository;

    public PorteVisaServiceImpl(PorteVisaRepository porteVisaRepository) {
        this.porteVisaRepository = porteVisaRepository;
    }




    @Override
    public PorteVisa findByEmail(String email) {
        return porteVisaRepository.findByEmail(email);
    }

    @Override
    public int deleteById(Long id) {
        if (!porteVisaRepository.existsById(id)) {
            return -1; // ID non trouvé
        } else {
            porteVisaRepository.deleteById(id);
            return 1; // suppression réussie
        }
    }

    @Override
    public List<PorteVisa> findAll() {
        return porteVisaRepository.findAll();
    }

    @Override
    public PorteVisa getPorteVisaById(Long id) {
        return porteVisaRepository.findById(id).orElse(null);
    }

    @Override
    public int updateComplet(String email, String codeCIN, LocalDate dateNaissance, String passportNumber, String lieuNaissance, String adresse, String genre, String nationalite, String destinationVisa, String typeVisa, String dureeSejour, LocalDate dateDelivrancePassport, LocalDate dateExpirationPassport) throws IOException {
        System.out.println("Début de updateComplet pour: " + email);
        PorteVisa porteVisa = porteVisaRepository.findByEmail(email);
        if (porteVisa == null) {
            System.out.println("porteVisa non trouvé: " + email);
            return -1;
        }
        if (codeCIN != null) porteVisa.setCodeCIN(codeCIN);
        if (dateNaissance != null) porteVisa.setDateNaissance(dateNaissance);
        if (lieuNaissance != null) porteVisa.setLieuNaissance(lieuNaissance);
        if (adresse != null) porteVisa.setAdresse(adresse);
        if (genre != null) porteVisa.setGenre(genre);
        if (nationalite != null) porteVisa.setNationalite(nationalite);
        if (passportNumber != null) porteVisa.setPassportNumber(passportNumber);
        if (dateDelivrancePassport != null) porteVisa.setDateDelivrancePassport(dateDelivrancePassport);
        if (dateExpirationPassport != null) porteVisa.setDateExpirationPassport(dateExpirationPassport);
        if (dureeSejour != null) porteVisa.setDureeSejour(dureeSejour);
        if (destinationVisa != null) porteVisa.setDestinationVisa(destinationVisa);
        if (typeVisa != null) porteVisa.setTypeVisa(typeVisa);

        porteVisaRepository.save(porteVisa);
        System.out.println("porteVisa sauvegardé avec succès.");
        return 0;

    }
}
