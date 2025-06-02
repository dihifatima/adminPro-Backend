package com.example.security.Authentification.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:5173",
        "http://127.0.0.1:3000",
        "http://127.0.0.1:5173"
}, allowCredentials = "true")
public class GoogleAuthController {

    private final GoogleTokenVerifierService googleTokenVerifierService;
    private final AuthenticateService authenticateService;

    @PostMapping("/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody GoogleAuthRequest request) {
        try {
            // Vérifier le token Google
            var payload = googleTokenVerifierService.verify(request.getIdToken());

            // Extraire email et infos
            String email = payload.getEmail();
            String firstname = (String) payload.get("given_name");
            String lastname = (String) payload.get("family_name");

            // Utiliser "particulier" comme rôle par défaut si userType n'est pas spécifié
            String userType = request.getUserType() != null ? request.getUserType() : "particulier";

            // Traiter l'utilisateur Google avec le type spécifié
            var authResponse = authenticateService.processGoogleUser(email, firstname, lastname, userType);

            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Erreur d'authentification Google: " + e.getMessage()));
        }
    }

    // Classe interne pour les erreurs
    private static class ErrorResponse {
        public String message;
        public ErrorResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
    }
}