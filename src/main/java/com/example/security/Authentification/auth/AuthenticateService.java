package com.example.security.Authentification.auth;

import com.example.security.Authentification.email.EmailService;
import com.example.security.Authentification.role.Role;
import com.example.security.Authentification.role.RoleRepository;
import com.example.security.Authentification.security.JwtService;
import com.example.security.Authentification.user.Token;
import com.example.security.Authentification.user.TokenRepository;
import com.example.security.Authentification.user.User;
import com.example.security.Authentification.user.UserRepository;
import com.example.security.entity.Admin;
import com.example.security.entity.Etudiant;
import com.example.security.repository.AdminRepository;
import com.example.security.repository.EtudiantRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticateService {
    private final RoleRepository roleRepository;
    private final EtudiantRepository etudiantRepository;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final FileService fileService;

    @Value("${app.file-storage-location}")
    private String fileStorageLocation;

    public ResponseEntity<?> register(RegistrationRequest request) throws MessagingException, IOException {
        Role userRole1;

        // Vérification du rôle et enregistrement d'un admin ou étudiant
        if (request.isAdmin()) {
            userRole1 = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new IllegalStateException("ROLE ADMIN was not initialized"));

            Admin admin = Admin.builder()
                    .email(request.getEmail())
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .roles(List.of(userRole1))
                    .password(passwordEncoder.encode(request.getPassword()))
                    .accountLocked(false)
                    .enabled(false)
                    .adminDepartement(request.getAdminDepartement())
                    .build();

            adminRepository.save(admin);
            emailService.sendValidationEmail(admin); // Envoi du mail de validation pour un admin
        } else if (request.isEtudiant()) {
            String userId = UUID.randomUUID().toString();

            String scanBacPath = null;
            String cinScanPath = null;
            String releveDeNotesPath = null;

            if (request.getScanBac() != null) {
                scanBacPath = saveFile(request.getScanBac(), userId + "_scanBac.pdf");
            }

            if (request.getCinScan() != null) {
                cinScanPath = saveFile(request.getCinScan(), userId + "_cinScan.pdf");
            }

            if (request.getReleveDeNotesScan() != null) {
                releveDeNotesPath = saveFile(request.getReleveDeNotesScan(), userId + "_releveNotes.pdf");
            }

            userRole1 = roleRepository.findByName("ETUDIANT")
                    .orElseThrow(() -> new IllegalStateException("ROLE ETUDIANT was not initialized"));

            Etudiant etudiant = Etudiant.builder()
                    .email(request.getEmail())
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .roles(List.of(userRole1))
                    .password(passwordEncoder.encode(request.getPassword()))
                    .accountLocked(false)
                    .enabled(false)
                    .niveauEtude(request.getNiveauEtude())
                    .filiere(request.getFiliere())
                    .etablissementActuel(request.getEtablissementActuel())
                    .scanBacPath(scanBacPath)
                    .cinScanPath(cinScanPath)
                    .releveDeNotesScanPath(releveDeNotesPath)
                    .build();

            // Enregistrement de l'étudiant
            etudiantRepository.save(etudiant);
            emailService.sendValidationEmail(etudiant); // Envoi du mail de validation pour un étudiant
        } else {
            throw new IllegalArgumentException("Invalid role selection");
        }

        // Réponse de succès après l'enregistrement
        Map<String, String> responseMessage = new HashMap<>();
        responseMessage.put("message", "Registration successful with role " + userRole1.getName() + " !");
        return ResponseEntity.accepted().body(responseMessage);
    }

    private String saveFile(byte[] data, String filename) throws IOException {
        Path uploadDir = Paths.get(fileStorageLocation);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        Path filePath = uploadDir.resolve(filename);
        Files.write(filePath, data);

        return filePath.toString();
    }

    @Transactional
    public void deleteAllUsers() {
        tokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    public AuthenticationResponse authenticate(AuthenticateRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var claims = new HashMap<String, Object>();
        var user = ((User) auth.getPrincipal());
        claims.put("fullName", user.getFullName());
        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException(("invalid token")));
        if (LocalDateTime.now().isAfter(savedToken.getExpiredAt())) {
            emailService.sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been send !");
        }
        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidateAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }
}