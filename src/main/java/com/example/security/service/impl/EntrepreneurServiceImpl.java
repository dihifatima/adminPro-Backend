package com.example.security.service.impl;
import com.example.security.dao.EntrepreneurRepository;
import com.example.security.entity.Entrepreneur;
import com.example.security.service.facade.EntrepreneurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class EntrepreneurServiceImpl implements EntrepreneurService {

    private final EntrepreneurRepository entrepreneurRepository;

    public EntrepreneurServiceImpl(EntrepreneurRepository entrepreneurRepository) {
        this.entrepreneurRepository = entrepreneurRepository;
    }

    @Override
    public int updateComplet(String email,
                             String cin,
                             String nomEntreprise,
                             String secteurActivite,
                             String registreCommerce,
                             String identifiantFiscal,
                             String typeEntreprise,
                             LocalDate dateCreation,
                             String siegeSocial) throws IOException {
        System.out.println("Début de updateComplet pour: " + email);
        Entrepreneur entrepreneur = entrepreneurRepository.findByEmail(email);
        if (entrepreneur == null) {
            System.out.println("Entrepreneur non trouvé: " + email);
            return -1;
        }
        if (cin != null) entrepreneur.setCin(cin);
        if (nomEntreprise != null) entrepreneur.setNomEntreprise(nomEntreprise);
        if (secteurActivite != null) entrepreneur.setSecteurActivite(secteurActivite);
        if (registreCommerce != null) entrepreneur.setRegistreCommerce(registreCommerce);
        if (identifiantFiscal != null) entrepreneur.setIdentifiantFiscal(identifiantFiscal);
        if (typeEntreprise != null) entrepreneur.setTypeEntreprise(typeEntreprise);
        if (dateCreation != null) entrepreneur.setDateCreation(dateCreation);
        if (siegeSocial != null) entrepreneur.setSiegeSocial(siegeSocial);
        entrepreneurRepository.save(entrepreneur);
        System.out.println("entrepreneur sauvegardé avec succès.");
        return 0;

    }

    @Override
    public Entrepreneur findByEmail(String email) {

        return entrepreneurRepository.findByEmail(email);
    }

    @Override
    public int deleteById(Long id) {
        if (!entrepreneurRepository.existsById(id)) {
            return -1; // ID non trouvé
        } else {
            entrepreneurRepository.deleteById(id);
            return 1; // suppression réussie
        }
    }

    @Override
    public List<Entrepreneur> findAll() {

        return entrepreneurRepository.findAll();
    }

    @Override
    public Entrepreneur getEntrepreneurById(Long id) {

        return entrepreneurRepository.findById(id).orElse(null);
    }
}