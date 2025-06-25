package com.example.security.Authentification.security;

import com.example.security.Authentification.role.Role;
import com.example.security.Authentification.role.RoleRepository;
import com.example.security.Authentification.user.User;
import com.example.security.Authentification.user.UserRepository;
import com.example.security.dao.*;
import com.example.security.entity.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AdminRepository adminRepository;
    private final EtudiantRepository etudiantRepository;
    private final EntrepreneurRepository entrepreneurRepository;
    private final ParticulierRepository particulierRepository;
    private final DemandeurVisaRepository demandeurVisaRepository;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // Extraire les informations de l'utilisateur Google
        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");

        // Récupérer le userType depuis le state
        String state = request.getParameter("state");
        String userType = extractUserTypeFromState(state);

        // Vérifier si l'utilisateur existe déjà
        Optional<User> existingUser = userRepository.findByEmail(email);

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            // Créer un nouvel utilisateur selon le type
            user = createUserByType(email, firstName, lastName, userType);
        }

        // Générer le token JWT
        Map<String, Object> claims = new HashMap<>();
        claims.put("fullName", user.getFullName());
        claims.put("telephone", user.getTelephone());
        String jwtToken = jwtService.generateToken(claims, user);

        // Rediriger vers le frontend avec le token
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth2/redirect")
                .queryParam("token", jwtToken)
                .queryParam("userType", userType)
                .build().toUriString();


        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String extractUserTypeFromState(String state) {
        if (state != null && state.contains("userType=")) {
            String[] parts = state.split("\\|");
            for (String part : parts) {
                if (part.startsWith("userType=")) {
                    return part.substring("userType=".length());
                }
            }
        }
        return "particulier"; // Valeur par défaut
    }

    private User createUserByType(String email, String firstName, String lastName, String userType) {
        // Normalisation : en minuscules et sans tirets
        userType = userType.toLowerCase().replace("-", "");

        Role userRole;

        switch (userType) {
            case "admin":
                userRole = roleRepository.findByName("ADMIN")
                        .orElseThrow(() -> new IllegalStateException("Le rôle ADMIN est introuvable"));
                Admin admin = Admin.builder()
                        .email(email)
                        .firstname(firstName)
                        .lastname(lastName)
                        .roles(List.of(userRole))
                        .password("") // Pas de mot de passe pour OAuth2
                        .accountLocked(false)
                        .enabled(true) // Activé directement pour OAuth2
                        .consentGDPR(true)
                        .build();
                return adminRepository.save(admin);

            case "etudiant":
                userRole = roleRepository.findByName("ETUDIANT")
                        .orElseThrow(() -> new IllegalStateException("Le rôle ETUDIANT est introuvable"));
                Etudiant etudiant = Etudiant.builder()
                        .email(email)
                        .firstname(firstName)
                        .lastname(lastName)
                        .roles(List.of(userRole))
                        .password("")
                        .accountLocked(false)
                        .enabled(true)
                        .consentGDPR(true)
                        .build();
                return etudiantRepository.save(etudiant);

            case "entrepreneur":
                userRole = roleRepository.findByName("ENTREPRENEUR")
                        .orElseThrow(() -> new IllegalStateException("Le rôle ENTREPRENEUR est introuvable"));
                Entrepreneur entrepreneur = Entrepreneur.builder()
                        .email(email)
                        .firstname(firstName)
                        .lastname(lastName)
                        .roles(List.of(userRole))
                        .password("")
                        .accountLocked(false)
                        .enabled(true)
                        .consentGDPR(true)
                        .build();
                return entrepreneurRepository.save(entrepreneur);

            case "demandeurvisa":  // <-- sans tiret et en minuscules
                userRole = roleRepository.findByName("DEMANDEURVISA")
                        .orElseThrow(() -> new IllegalStateException("Le rôle DEMANDEURVISA est introuvable"));
                DemandeurVisa demandeurVisa = DemandeurVisa.builder()
                        .email(email)
                        .firstname(firstName)
                        .lastname(lastName)
                        .roles(List.of(userRole))
                        .password("")
                        .accountLocked(false)
                        .enabled(true)
                        .consentGDPR(true)
                        .build();
                return demandeurVisaRepository.save(demandeurVisa);

            case "particulier":
            default:
                userRole = roleRepository.findByName("PARTICULIER")
                        .orElseThrow(() -> new IllegalStateException("Le rôle PARTICULIER est introuvable"));
                Particulier particulier = Particulier.builder()
                        .email(email)
                        .firstname(firstName)
                        .lastname(lastName)
                        .roles(List.of(userRole))
                        .password("")
                        .accountLocked(false)
                        .enabled(true)
                        .consentGDPR(true)
                        .build();
                return particulierRepository.save(particulier);
        }
    }
}