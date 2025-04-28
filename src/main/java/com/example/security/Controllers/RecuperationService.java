package com.example.security.Controllers;

import com.example.security.entity.Etudiant;
import com.example.security.repository.EtudiantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecuperationService {

    private final EtudiantRepository etudiantRepository;

    public List<RecuperationEtudiantResponse> getAllEtudiants() {
        List<Etudiant> etudiants = etudiantRepository.findAll();
        return etudiants.stream()
                .map(this::mapEtudiantToResponse)
                .collect(Collectors.toList());
    }

    public RecuperationEtudiantResponse mapEtudiantToResponse(Etudiant etudiant) {
        return RecuperationEtudiantResponse.builder()
                .id(etudiant.getId())
                .firstname(etudiant.getFirstname())
                .lastname(etudiant.getLastname())
                .email(etudiant.getEmail())
                .role(etudiant.getRoles().isEmpty() ? null : etudiant.getRoles().get(0).getName())
                .niveauEtude(etudiant.getNiveauEtude())
                .filiere(etudiant.getFiliere())
                .etablissementActuel(etudiant.getEtablissementActuel())
                .scanBac(Arrays.toString(etudiant.getScanBac()))
                .cinScan(Arrays.toString(etudiant.getCinScan()))
                .releveDeNotesScan(Arrays.toString(etudiant.getReleveDeNotesScan()))
                .build();
    }
}
