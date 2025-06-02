package com.example.security.Authentification.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory; // Changé de JacksonFactory à GsonFactory
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;
import java.io.IOException;
import java.util.Collections;

@Service
public class GoogleTokenVerifierService {

    @Value("${google.client-id}")
    private String clientId;

    private GoogleIdTokenVerifier verifier;

    private GoogleIdTokenVerifier getVerifier() throws Exception {
        if (verifier == null) {
            verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance()) // Changé vers GsonFactory
                    .setAudience(Collections.singletonList(clientId))
                    .build();
        }
        return verifier;
    }

    public GoogleIdToken.Payload verify(String idTokenString) throws GeneralSecurityException, IOException, Exception {
        GoogleIdToken idToken = getVerifier().verify(idTokenString);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            if (!payload.getEmailVerified()) {
                throw new IllegalArgumentException("Email Google non vérifié");
            }

            return payload;
        } else {
            throw new IllegalArgumentException("Token Google invalide ou expiré");
        }
    }
}