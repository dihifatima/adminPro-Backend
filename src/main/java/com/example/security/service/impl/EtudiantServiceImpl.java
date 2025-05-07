package com.example.security.service.impl;

import com.example.security.dao.EtudiantRepository;
import com.example.security.entity.Etudiant;
import com.example.security.service.facade.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
@Service
public class EtudiantServiceImpl implements EtudiantService {
    @Autowired
    private final EtudiantRepository etudiantRepository;
    public EtudiantServiceImpl(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }
    /*@Override
    public int update(Etudiant etudiant) {
        Etudiant existing = findByEmail(etudiant.getEmail());
        if (existing == null) return -1;

        // Mise à jour uniquement des champs non nuls
        if (etudiant.getFirstname() != null) existing.setFirstname(etudiant.getFirstname());
        if (etudiant.getLastname() != null) existing.setLastname(etudiant.getLastname());
        if (etudiant.getNiveauEtude() != null) existing.setNiveauEtude(etudiant.getNiveauEtude());
        if (etudiant.getFiliere() != null) existing.setFiliere(etudiant.getFiliere());
        if (etudiant.getEtablissementActuel() != null) existing.setEtablissementActuel(etudiant.getEtablissementActuel());
        if (etudiant.getScanBacPath() != null) existing.setScanBacPath(etudiant.getScanBacPath());
        if (etudiant.getCinScanPath() != null) existing.setCinScanPath(etudiant.getCinScanPath());
        if (etudiant.getPhotos() != null) existing.setPhotos(etudiant.getPhotos());
        if (etudiant.getReleveDeNotesScanPath() != null) existing.setReleveDeNotesScanPath(etudiant.getReleveDeNotesScanPath());

        etudiantRepository.save(existing);
        return 0;
    }*/
    @Override
    public int updateComplet(String email, String niveauEtude, String filiere, String etablissementActuel,
                             MultipartFile scanBac, MultipartFile cinScan, MultipartFile photos, MultipartFile releveNotes) throws IOException {

        Etudiant etudiant = etudiantRepository.findByEmail(email);
        if (etudiant == null) return -1;

        if (niveauEtude != null) etudiant.setNiveauEtude(niveauEtude);
        if (filiere != null) etudiant.setFiliere(filiere);
        if (etablissementActuel != null) etudiant.setEtablissementActuel(etablissementActuel);

        Path uploadPath = Paths.get("uploads/etudiants").toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);
        if (scanBac != null && !scanBac.isEmpty()) {
            System.out.println("Téléchargement du fichier scanBac");
            etudiant.setScanBacPath(saveFile(uploadPath, scanBac));
        }
        if (cinScan != null && !cinScan.isEmpty()) {
            System.out.println("Téléchargement du fichier cinScan");
            etudiant.setCinScanPath(saveFile(uploadPath, cinScan));
        }
        if (photos != null && !photos.isEmpty()) {
            System.out.println("Téléchargement du fichier photos");
            etudiant.setPhotos(saveFile(uploadPath, photos));
        }
        if (releveNotes != null && !releveNotes.isEmpty()) {
            System.out.println("Téléchargement du fichier releveNotes");
            etudiant.setReleveDeNotesScanPath(saveFile(uploadPath, releveNotes));
        }
        etudiantRepository.save(etudiant);
        return 0;
    }

    private String saveFile(Path uploadDir, MultipartFile file) throws IOException {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = System.currentTimeMillis() + "_" + originalFilename;
        Path filePath = uploadDir.resolve(fileName);
        // Log pour débogage
        System.out.println("Enregistrement du fichier : " + originalFilename + " de type " + file.getContentType());

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // إرجاع المسار النسبي: "uploads/etudiants/..."
        return "uploads/etudiants/" + fileName;
    }

    @Override
    public Etudiant findByEmail(String email) {
        return etudiantRepository.findByEmail(email);
    }

    @Override
    @Transactional

    public int deleteById(Long id) {
        if (!etudiantRepository.existsById(id)) {
            return -1; // ID non trouvé
        } else {
            etudiantRepository.deleteById(id);
            return 1; // suppression réussie
        }
    }

    @Override
    public List<Etudiant> findAll() {
        return etudiantRepository.findAll();
    }

    @Override
    public Etudiant getEtudiantById(Long id) {
        return etudiantRepository.findById(id).orElse(null);
    }

    @Override
    public List<Etudiant> findByFirstnameOrLastname(String firstname, String lastname) {
        return etudiantRepository.findByFirstnameOrLastname(firstname, lastname);
    }




}



