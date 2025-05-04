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


@Service
@RequiredArgsConstructor
public class AuthenticateService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final AdminRepository adminRepository;
    private final EtudiantRepository  etudiantRepository;
    private final PorteVisaRepository porteVisaRepository;
    private final EntrepreneurRepository entrepreneurRepository;
    private final AdminSecondaireRepository adminSecondaireRepository;
    private final ParticulierRepository particulierRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;



    public ResponseEntity<?> register(RegistrationRequest request) throws MessagingException {
        Role userRole;

        // Vérification du rôle et enregistrement d'un admin ou étudiant
        if (request.isAdmin()) {
/*Ce if vérifie si l'utilisateur que l'on souhaite enregistrer est un Admin. request.isAdmin() renvoie true si l'utilisateur a choisi le rôle "Admin" lors de l'enregistrement. Si ce n'est pas le cas, la logique continue avec le else if pour vérifier si l'utilisateur est un Etudiant.*/
            userRole= roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new IllegalStateException("Le rôle ADMIN est introuvable ou non défini dans la base de données"));
/*Cette ligne récupère le rôle "ADMIN" dans la base de données via roleRepository. Si le rôle n'est pas trouvé, une exception IllegalStateException est levée avec le message "ROLE ADMIN was not initialized". Cette vérification garantit que le rôle "ADMIN" existe dans la base de données avant de créer un utilisateur admin.*/
//creation de l 'instance Admin  via le builder pattern
            Admin admin = Admin.builder()
                    .email(request.getEmail())
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .roles(List.of(userRole))
                    .password(passwordEncoder.encode(request.getPassword()))
                    .accountLocked(false)
                    .enabled(false)
                    .build();

            adminRepository.save(admin);//Enregistrement de l'Admin dans la base de données
            emailService.sendValidationEmail(admin); // Envoi du mail de validation pour un admin
         }else if (request.isAdminSecondaire()) {
            userRole = roleRepository.findByName("ADMINSECONDAIRE")
                    .orElseThrow(() -> new IllegalStateException("Le rôle ADMINSECONDAIRE est introuvable ou non défini dans la base de données"));

            AdminSecondaire adminSecondaire = AdminSecondaire.builder()
                    .email(request.getEmail())
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .roles(List.of(userRole))
                    .password(passwordEncoder.encode(request.getPassword()))
                    .accountLocked(false)
                    .enabled(false)
                    .build();

            adminSecondaireRepository.save(adminSecondaire);
            emailService.sendValidationEmail(adminSecondaire); // Envoi du mail de validation pour un admin secondaire
        }

        else if (request.isEtudiant()) {
            userRole = roleRepository.findByName("ETUDIANT")
                    .orElseThrow(() -> new IllegalStateException("Le rôle ETUDIANT est introuvable ou non défini dans la base de données"));

            Etudiant etudiant = Etudiant.builder()
                    .email(request.getEmail())
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .roles(List.of(userRole))
                    .password(passwordEncoder.encode(request.getPassword()))
                    .accountLocked(false)
                    .enabled(false)
                    .build();

            // Enregistrement de l'étudiant
            etudiantRepository.save(etudiant);
            emailService.sendValidationEmail(etudiant); // Envoi du mail de validation pour un étudiant
        }

        else if (request.isEntrepreneur()) {
            userRole = roleRepository.findByName("ENTREPRENEUR")
                    .orElseThrow(() -> new IllegalStateException("Le rôle ENTREPRENEUR est introuvable ou non défini dans la base de données"));

            Entrepreneur entrepreneur = Entrepreneur.builder()
                    .email(request.getEmail())
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .roles(List.of(userRole))
                    .password(passwordEncoder.encode(request.getPassword()))
                    .accountLocked(false)
                    .enabled(false)
                    .build();

            entrepreneurRepository.save(entrepreneur);
            emailService.sendValidationEmail(entrepreneur); // Envoi du mail de validation pour un entrepreneur
        }else if (request.isParticulier()) {
            userRole = roleRepository.findByName("PARTICULIER")
                    .orElseThrow(() -> new IllegalStateException("Le rôle PARTICULIER est introuvable ou non défini dans la base de données"));

            Particulier particulier = Particulier.builder()
                    .email(request.getEmail())
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .roles(List.of(userRole))
                    .password(passwordEncoder.encode(request.getPassword()))
                    .accountLocked(false)
                    .enabled(false)
                    .build();

            particulierRepository.save(particulier);
            emailService.sendValidationEmail(particulier); // Envoi du mail de validation pour un particulier
        }
        else if (request.isPorteVisa()) {
            userRole = roleRepository.findByName("PORTEVISA")
                    .orElseThrow(() -> new IllegalStateException("Le rôle PORTEVISA est introuvable ou non défini dans la base de données"));

            PorteVisa porteVisa = PorteVisa.builder()
                    .email(request.getEmail())
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .roles(List.of(userRole))
                    .password(passwordEncoder.encode(request.getPassword()))
                    .accountLocked(false)
                    .enabled(false)
                    .build();

            porteVisaRepository.save(porteVisa);
            emailService.sendValidationEmail(porteVisa); // Envoi du mail de validation pour un porteur de visa
        }


        else {
            throw new IllegalArgumentException("Invalid role selection");
        }

        // Réponse de succès après l'enregistrement
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