package com.example.security.Authentification.auth;

import com.example.security.Authentification.email.EmailService;
import com.example.security.Authentification.role.Role;
import com.example.security.Authentification.role.RoleRepository;
import com.example.security.Authentification.security.JwtService;
import com.example.security.Authentification.user.Token;
import com.example.security.Authentification.user.TokenRepository;
import com.example.security.Authentification.user.User;
import com.example.security.Authentification.user.UserRepository;
import com.example.security.dao.*;
import com.example.security.entity.*;
import com.example.security.exception.CustomException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticateService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final AdminRepository adminRepository;
    private final EtudiantRepository etudiantRepository;
    private final DemandeurVisaRepository porteVisaRepository;
    private final EntrepreneurRepository entrepreneurRepository;
    private final ParticulierRepository particulierRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public ResponseEntity<?> register(RegistrationRequest request) throws MessagingException {
        Role userRole;

        if (request.isAdmin()) {
            userRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new IllegalStateException("Le rôle ADMIN est introuvable ou non défini dans la base de données"));
            Admin admin = Admin.builder()
                    .email(request.getEmail())
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .telephone(request.getTelephone())
                    .roles(List.of(userRole))
                    .password(passwordEncoder.encode(request.getPassword()))
                    .accountLocked(false)
                    .enabled(false)
                    .consentGDPR(request.isConsentGDPR())
                    .build();

            adminRepository.save(admin);
            emailService.sendValidationEmail(admin);
        } else if (request.isEtudiant()) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new CustomException("Cet email est déjà utilisé");
            }
            userRole = roleRepository.findByName("ETUDIANT")
                    .orElseThrow(() -> new IllegalStateException("Le rôle ETUDIANT est introuvable ou non défini dans la base de données"));

            Etudiant etudiant = Etudiant.builder()
                    .email(request.getEmail())
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .telephone(request.getTelephone())
                    .roles(List.of(userRole))
                    .password(passwordEncoder.encode(request.getPassword()))
                    .accountLocked(false)
                    .consentGDPR(request.isConsentGDPR())
                    .enabled(false)
                    .build();

            etudiantRepository.save(etudiant);
            emailService.sendValidationEmail(etudiant);
        } else if (request.isEntrepreneur()) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new CustomException("Cet email est déjà utilisé");
            }
            userRole = roleRepository.findByName("ENTREPRENEUR")
                    .orElseThrow(() -> new IllegalStateException("Le rôle ENTREPRENEUR est introuvable ou non défini dans la base de données"));

            Entrepreneur entrepreneur = Entrepreneur.builder()
                    .email(request.getEmail())
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .telephone(request.getTelephone())
                    .roles(List.of(userRole))
                    .password(passwordEncoder.encode(request.getPassword()))
                    .accountLocked(false)
                    .consentGDPR(request.isConsentGDPR())
                    .enabled(false)
                    .build();

            entrepreneurRepository.save(entrepreneur);
            emailService.sendValidationEmail(entrepreneur);
        } else if (request.isParticulier()) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new CustomException("Cet email est déjà utilisé");
            }
            userRole = roleRepository.findByName("PARTICULIER")
                    .orElseThrow(() -> new IllegalStateException("Le rôle PARTICULIER est introuvable ou non défini dans la base de données"));

            Particulier particulier = Particulier.builder()
                    .email(request.getEmail())
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .telephone(request.getTelephone())
                    .roles(List.of(userRole))
                    .password(passwordEncoder.encode(request.getPassword()))
                    .accountLocked(false)
                    .consentGDPR(request.isConsentGDPR())
                    .enabled(false)
                    .build();

            particulierRepository.save(particulier);
            emailService.sendValidationEmail(particulier);
        } else if (request.isPorteVisa()) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new CustomException("Cet email est déjà utilisé");
            }
            userRole = roleRepository.findByName("PORTEVISA")
                    .orElseThrow(() -> new IllegalStateException("Le rôle PORTEVISA est introuvable ou non défini dans la base de données"));

            DemandeurVisa porteVisa = DemandeurVisa.builder()
                    .email(request.getEmail())
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .telephone(request.getTelephone())
                    .roles(List.of(userRole))
                    .password(passwordEncoder.encode(request.getPassword()))
                    .accountLocked(false)
                    .consentGDPR(request.isConsentGDPR())
                    .enabled(false)
                    .build();

            porteVisaRepository.save(porteVisa);
            emailService.sendValidationEmail(porteVisa);
        } else {
            throw new IllegalArgumentException("Invalid role selection");
        }

        Map<String, String> responseMessage = new HashMap<>();
        responseMessage.put("message", "Registration successful with role " + userRole.getName() + " !");
        return ResponseEntity.accepted().body(responseMessage);
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
        claims.put("telephone", user.getTelephone());
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

    /**
     * Traite l'authentification Google OAuth avec choix de rôle
     * Crée un nouvel utilisateur selon le rôle spécifié ou active le compte existant
     */
    @Transactional // AJOUT: Assure que la transaction est complète avant de générer le JWT
    public AuthenticationResponse processGoogleUser(String email, String firstname, String lastname, String userType) {
        Optional<User> existingUserOpt = userRepository.findByEmail(email);

        User user;
        if (existingUserOpt.isPresent()) {
            // Utilisateur existant - on active son compte si nécessaire
            user = existingUserOpt.get();

            if (!user.isEnabled()) {
                user.setEnabled(true);
                user = userRepository.save(user); // MODIF: Assigner le résultat du save
            }
        } else {
            // Nouvel utilisateur Google - création selon le type spécifié
            user = createGoogleUser(email, firstname, lastname, userType);
        }

        // Force le flush pour s'assurer que l'utilisateur est en base
        userRepository.flush();

        // Génération du JWT avec les mêmes claims que l'authentification normale
        var claims = new HashMap<String, Object>();
        claims.put("fullName", user.getFullName());
        claims.put("telephone", user.getTelephone());

        String jwtToken = jwtService.generateToken(claims, user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * Crée un utilisateur Google selon le type spécifié
     */
    private User createGoogleUser(String email, String firstname, String lastname, String userType) {
        String roleName;
        User newUser;

        switch (userType.toLowerCase()) {
            case "admin":
                roleName = "ADMIN";
                Role adminRole = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));

                Admin admin = Admin.builder()
                        .email(email)
                        .firstname(firstname)
                        .lastname(lastname)
                        .enabled(true)
                        .accountLocked(false)
                        .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .roles(List.of(adminRole))
                        .consentGDPR(true)
                        .build();
                newUser = adminRepository.saveAndFlush(admin); // MODIF: saveAndFlush au lieu de save
                break;

            case "etudiant":
                roleName = "ETUDIANT";
                Role etudiantRole = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role ETUDIANT not found"));

                Etudiant etudiant = Etudiant.builder()
                        .email(email)
                        .firstname(firstname)
                        .lastname(lastname)
                        .enabled(true)
                        .accountLocked(false)
                        .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .roles(List.of(etudiantRole))
                        .consentGDPR(true)
                        .build();
                newUser = etudiantRepository.saveAndFlush(etudiant); // MODIF: saveAndFlush au lieu de save
                break;

            case "entrepreneur":
                roleName = "ENTREPRENEUR";
                Role entrepreneurRole = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role ENTREPRENEUR not found"));

                Entrepreneur entrepreneur = Entrepreneur.builder()
                        .email(email)
                        .firstname(firstname)
                        .lastname(lastname)
                        .enabled(true)
                        .accountLocked(false)
                        .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .roles(List.of(entrepreneurRole))
                        .consentGDPR(true)
                        .build();
                newUser = entrepreneurRepository.saveAndFlush(entrepreneur); // MODIF: saveAndFlush au lieu de save
                break;

            case "portevisa":
                roleName = "PORTEVISA";
                Role porteVisaRole = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role PORTEVISA not found"));

                DemandeurVisa porteVisa = DemandeurVisa.builder()
                        .email(email)
                        .firstname(firstname)
                        .lastname(lastname)
                        .enabled(true)
                        .accountLocked(false)
                        .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .roles(List.of(porteVisaRole))
                        .consentGDPR(true)
                        .build();
                newUser = porteVisaRepository.saveAndFlush(porteVisa); // MODIF: saveAndFlush au lieu de save
                break;

            case "particulier":
            default:
                roleName = "PARTICULIER";
                Role particulierRole = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role PARTICULIER not found"));

                Particulier particulier = Particulier.builder()
                        .email(email)
                        .firstname(firstname)
                        .lastname(lastname)
                        .enabled(true)
                        .accountLocked(false)
                        .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .roles(List.of(particulierRole))
                        .consentGDPR(true)
                        .build();
                newUser = particulierRepository.saveAndFlush(particulier); // MODIF: saveAndFlush au lieu de save
                break;
        }

        return newUser;
    }
}