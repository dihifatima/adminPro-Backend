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

    @Override
    public int updateComplet(String email, String niveauEtude, String filiere, String etablissementActuel,
                             MultipartFile scanBac, MultipartFile cinScan, MultipartFile photos,
                             MultipartFile releveDeNotesScan) throws IOException {

        System.out.println("Début de updateComplet pour: " + email);
//🔸 نبحث في قاعدة البيانات عن الطالب بهذا الإيميل. إذا لم يُعثر عليه، تُرجع -1
        Etudiant etudiant = etudiantRepository.findByEmail(email);
        if (etudiant == null) {
            System.out.println("Étudiant non trouvé: " + email);
            return -1;
        }

        if (niveauEtude != null) etudiant.setNiveauEtude(niveauEtude);
        if (filiere != null) etudiant.setFiliere(filiere);
        if (etablissementActuel != null) etudiant.setEtablissementActuel(etablissementActuel);

        // S'assurer que le répertoire existe
        Path uploadPath = Paths.get("uploads/etudiants").toAbsolutePath();
        Files.createDirectories(uploadPath);
        System.out.println("Répertoire de stockage: " + uploadPath);

        // Traitement des fichiers
        try {
            if (scanBac != null && !scanBac.isEmpty()) {
                String path = saveFile(uploadPath, scanBac);
                etudiant.setScanBac(path);
                System.out.println("Bac sauvegardé: " + path);
            }

            if (cinScan != null && !cinScan.isEmpty()) {
                String path = saveFile(uploadPath, cinScan);
                etudiant.setCinScan(path);
                System.out.println("CIN sauvegardé: " + path);
            }

            if (photos != null && !photos.isEmpty()) {
                String path = saveFile(uploadPath, photos);
                etudiant.setPhotos(path);
                System.out.println("Photo sauvegardée: " + path);
            }

            if (releveDeNotesScan != null && !releveDeNotesScan.isEmpty()) {
                String path = saveFile(uploadPath, releveDeNotesScan);
                etudiant.setReleveDeNotesScan(path);
                System.out.println("Relevé de notes sauvegardé: " + path);
            }

            etudiantRepository.save(etudiant);
            System.out.println("Étudiant sauvegardé avec succès.");
            return 0;
        } catch (Exception e) {
            System.err.println("Erreur pendant la sauvegarde des fichiers: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private String saveFile(Path uploadDir, MultipartFile file) throws IOException {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = System.currentTimeMillis() + "_" + originalFilename;
        Path filePath = uploadDir.resolve(fileName);

        System.out.println("Sauvegarde du fichier: " + originalFilename + " vers " + filePath);

        // Copie le fichier avec remplacement si existe déjà
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Vérification
        if (Files.exists(filePath)) {
            System.out.println("Fichier créé avec succès: " + filePath);
        } else {
            System.out.println("ERREUR: Le fichier n'a pas été créé: " + filePath);
        }

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