package com.example.security.service.impl;
import com.example.security.dao.ParticulierRepository;
import com.example.security.entity.Particulier;
import com.example.security.service.facade.ParticulierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
@Service
public class ParticulierServiceImpl implements ParticulierService {
    @Autowired
    private final ParticulierRepository particulierRepository;

    public ParticulierServiceImpl(ParticulierRepository particulierRepository) {
        this.particulierRepository = particulierRepository;
    }

    @Override
    public int updateComplet(String email,
                             String objetDemande,
                             String ville,
                             String adresse,
                             String codeCIN,
                             String lieuNaissance,
                             String nationalite,
                             String genre,
                             LocalDate dateNaissance) throws IOException {
        System.out.println("Début de updateComplet pour: " + email);
        Particulier particulier = particulierRepository.findByEmail(email);
        if (particulier == null) {
            System.out.println("Particulier non trouvé: " + email);
            return -1;
        }
        if (codeCIN != null) particulier.setCodeCIN(codeCIN);
        if (dateNaissance != null) particulier.setDateNaissance(dateNaissance);
        if (lieuNaissance != null) particulier.setLieuNaissance(lieuNaissance);
        if (adresse != null) particulier.setAdresse(adresse);
        if (genre != null) particulier.setGenre(genre);
        if (objetDemande != null) particulier.setObjetDemande(objetDemande);
        if (nationalite != null) particulier.setNationalite(nationalite);
        if (ville != null) particulier.setVille(ville);
        particulierRepository.save(particulier);
        System.out.println("Particulier sauvegardé avec succès.");
        return 0;

    }



    @Override
    public Particulier findByEmail(String email) {
        return particulierRepository.findByEmail(email);
    }

    @Override
    public int deleteById(Long id) {
        if (!particulierRepository.existsById(id)) {
            return -1;
        } else {
            particulierRepository.deleteById(id);
            return 1;
        }    }

    @Override
    public List<Particulier> findAll() {
        return particulierRepository.findAll();
                }

    @Override
    public Particulier getParticulierById(Long id) {
        return particulierRepository.findById(id).orElse(null);
    }
}
