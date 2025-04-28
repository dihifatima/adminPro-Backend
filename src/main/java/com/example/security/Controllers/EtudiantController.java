package com.example.security.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/etudiants")
@RequiredArgsConstructor
public class EtudiantController {

    private final RecuperationService recuperationService;

    @GetMapping("/all")
    public ResponseEntity<List<RecuperationEtudiantResponse>> getAllEtudiants() {
        List<RecuperationEtudiantResponse> etudiants = recuperationService.getAllEtudiants();
        return ResponseEntity.ok(etudiants);
    }
}
