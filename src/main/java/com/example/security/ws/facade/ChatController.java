package com.example.security.ws.facade;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://127.0.0.1:3000", "http://127.0.0.1:5173"},
        allowCredentials = "true",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Value("${gemini.api.key}")
    private String apiKey;

    @PostMapping
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> request) {
        try {
            String message = request.get("message");
            logger.info("Requête de chat reçue avec le message: {}", message);

            if (message == null || message.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Message ne peut pas être vide"));
            }

            // Vérifier que la clé API est configurée
            if (apiKey == null || apiKey.trim().isEmpty() || apiKey.equals("${gemini.api.key}")) {
                logger.error("Clé API Gemini non configurée");
                return ResponseEntity.status(500).body(Map.of("error", "Clé API Gemini non configurée"));
            }

            // Échapper les caractères spéciaux dans le message
            String escapedMessage = message.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");

            // Construire le JSON de requête
            String requestBody = String.format("""
                {
                  "contents": [
                    {
                      "parts": [
                        {
                          "text": "%s"
                        }
                      ]
                    }
                  ]
                }
                """, escapedMessage);

            logger.info("Envoi de la requête à Gemini API");

            // Créer la requête HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=" + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // Envoyer la requête et récupérer la réponse
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            logger.info("Réponse reçue avec le code de statut: {}", response.statusCode());

            if (response.statusCode() != 200) {
                logger.error("Erreur de l'API Gemini. Code de statut: {}, Body: {}", response.statusCode(), response.body());
                return ResponseEntity.status(500).body(Map.of("error", "Erreur de l'API Gemini: " + response.statusCode()));
            }

            // Parser la réponse JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response.body());

            logger.info("Réponse JSON reçue: {}", response.body());

            // Extraire la réponse
            String reply = null;
            try {
                reply = jsonNode
                        .path("candidates")
                        .get(0)
                        .path("content")
                        .path("parts")
                        .get(0)
                        .path("text")
                        .asText();
            } catch (Exception e) {
                logger.error("Erreur lors de l'extraction de la réponse: {}", e.getMessage());
                logger.error("Structure JSON reçue: {}", response.body());
            }

            if (reply == null || reply.trim().isEmpty()) {
                logger.error("Réponse vide de l'API. Structure JSON: {}", response.body());
                return ResponseEntity.status(500).body(Map.of("error", "Réponse vide de l'API"));
            }

            logger.info("Réponse générée avec succès");
            return ResponseEntity.ok(Map.of("reply", reply));

        } catch (IOException e) {
            logger.error("Erreur d'entrée/sortie lors de l'appel à l'API Gemini", e);
            return ResponseEntity.status(500).body(Map.of("error", "Erreur de communication avec l'API"));
        } catch (InterruptedException e) {
            logger.error("Requête interrompue", e);
            Thread.currentThread().interrupt();
            return ResponseEntity.status(500).body(Map.of("error", "Requête interrompue"));
        } catch (Exception e) {
            logger.error("Erreur inattendue", e);
            return ResponseEntity.status(500).body(Map.of("error", "Erreur interne du serveur: " + e.getMessage()));
        }
    }
}