package com.example.security.Authentification.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {
    private final AuthenticateService authenticateService;

    @PostMapping(path = "/register", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestPart("request") @Valid RegistrationRequest request,
            @RequestPart(value = "scanBac", required = false) MultipartFile scanBac,
            @RequestPart(value = "cinScan", required = false) MultipartFile cinScan,
            @RequestPart(value = "releveDeNotesScan", required = false) MultipartFile releveDeNotesScan
    ) throws MessagingException, IOException {

        // Vérification des fichiers et leur affectation
        if (scanBac != null && !scanBac.isEmpty()) {
            request.setScanBac(scanBac.getBytes());
        }
        if (cinScan != null && !cinScan.isEmpty()) {
            request.setCinScan(cinScan.getBytes());
        }
        if (releveDeNotesScan != null && !releveDeNotesScan.isEmpty()) {
            request.setReleveDeNotesScan(releveDeNotesScan.getBytes());
        }

        return authenticateService.register(request);
    }


    @DeleteMapping("/")
    public void deleteAllUsers(){
        authenticateService.deleteAllUsers();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticateRequest request
    ){
     return ResponseEntity.ok(authenticateService.authenticate(request));
    }

    @GetMapping("/activate-account")
    public void confirm(
            @RequestParam String token
    ) throws MessagingException {
        authenticateService.activateAccount(token);
    }


}
