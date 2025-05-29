package com.example.security.service.impl;
import com.example.security.dao.DemandeurVisaRepository;
import com.example.security.entity.DemandeurVisa;
import com.example.security.service.facade.DemandeurVisaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
@Service
public class DemandeurVisaServiceImpl implements DemandeurVisaService {

    private final DemandeurVisaRepository demandeurVisaRepository;

    public DemandeurVisaServiceImpl(DemandeurVisaRepository porteVisaRepository, DemandeurVisaRepository demandeurVisaRepository) {
        this.demandeurVisaRepository = demandeurVisaRepository;
    }

    @Override
    public DemandeurVisa findByEmail(String email) {

        return demandeurVisaRepository.findByEmail(email);
    }

    @Override
    public int deleteById(Long id) {
        if (!demandeurVisaRepository.existsById(id)) {
            return -1; // ID non trouvé
        } else {
            demandeurVisaRepository.deleteById(id);
            return 1; // suppression réussie
        }
    }

    @Override
    public List<DemandeurVisa> findAll() {

        return demandeurVisaRepository.findAll();
    }

    @Override
    public DemandeurVisa getDemandeurVisaById(Long id) {

        return demandeurVisaRepository.findById(id).orElse(null);
    }

    @Override
    public int updateComplet(String email, String codeCIN, LocalDate dateNaissance, String passportNumber, String lieuNaissance, String adresse, String genre, String nationalite, String destinationVisa, String typeVisa, String dureeSejour, LocalDate dateDelivrancePassport, LocalDate dateExpirationPassport) throws IOException {
        System.out.println("Début de updateComplet pour: " + email);
        DemandeurVisa demandeurVisa = demandeurVisaRepository.findByEmail(email);
        if (demandeurVisa == null) {
            System.out.println("demandeur visa non trouvé: " + email);
            return -1;
        }
        if (codeCIN != null) demandeurVisa.setCodeCIN(codeCIN);
        if (dateNaissance != null) demandeurVisa.setDateNaissance(dateNaissance);
        if (lieuNaissance != null) demandeurVisa.setLieuNaissance(lieuNaissance);
        if (adresse != null) demandeurVisa.setAdresse(adresse);
        if (genre != null) demandeurVisa.setGenre(genre);
        if (nationalite != null) demandeurVisa.setNationalite(nationalite);
        if (passportNumber != null) demandeurVisa.setPassportNumber(passportNumber);
        if (dateDelivrancePassport != null) demandeurVisa.setDateDelivrancePassport(dateDelivrancePassport);
        if (dateExpirationPassport != null) demandeurVisa.setDateExpirationPassport(dateExpirationPassport);
        if (dureeSejour != null) demandeurVisa.setDureeSejour(dureeSejour);
        if (destinationVisa != null) demandeurVisa.setDestinationVisa(destinationVisa);
        if (typeVisa != null) demandeurVisa.setTypeVisa(typeVisa);
        demandeurVisaRepository.save(demandeurVisa);
        System.out.println("demandeur visa  sauvegardé avec succès.");
        return 0;

    }
}
