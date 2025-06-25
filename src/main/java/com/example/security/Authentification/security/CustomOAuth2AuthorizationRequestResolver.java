package com.example.security.Authentification.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomOAuth2AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final OAuth2AuthorizationRequestResolver defaultResolver;

    public CustomOAuth2AuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository, "/oauth2/authorization");
        log.info("CustomOAuth2AuthorizationRequestResolver initialisé");
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        log.info("Résolution de la requête OAuth2: {}", request.getRequestURL());
        OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request);
        return customizeAuthorizationRequest(authorizationRequest, request);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        log.info("Résolution de la requête OAuth2 avec clientId: {} pour URL: {}",
                clientRegistrationId, request.getRequestURL());
        OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request, clientRegistrationId);
        return customizeAuthorizationRequest(authorizationRequest, request);
    }

    private OAuth2AuthorizationRequest customizeAuthorizationRequest(
            OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request) {

        if (authorizationRequest == null) {
            log.warn("AuthorizationRequest est null");
            return null;
        }

        // Log des paramètres de la requête
        log.info("Paramètres de la requête: {}", request.getParameterMap());

        // Capturer le userType depuis les paramètres
        String userType = request.getParameter("userType");
        log.info("UserType capturé: {}", userType);

        if (userType != null && !userType.trim().isEmpty()) {
            // Ajouter le userType dans le state pour le récupérer après l'authentification
            String originalState = authorizationRequest.getState();
            String newState = originalState + "|userType=" + userType;

            log.info("State original: {}", originalState);
            log.info("Nouveau state: {}", newState);

            OAuth2AuthorizationRequest customRequest = OAuth2AuthorizationRequest.from(authorizationRequest)
                    .state(newState)
                    // Forcer l'affichage de l'écran de consentement
                    .additionalParameters(params -> {
                        params.put("prompt", "consent");
                        params.put("access_type", "offline");
                    })
                    .build();

            log.info("URL d'autorisation générée: {}", customRequest.getAuthorizationUri());
            return customRequest;
        }

        // Même sans userType, forcer l'affichage du consentement pour debug
        OAuth2AuthorizationRequest customRequest = OAuth2AuthorizationRequest.from(authorizationRequest)
                .additionalParameters(params -> {
                    params.put("prompt", "consent");
                    params.put("access_type", "offline");
                })
                .build();

        log.info("URL d'autorisation générée (sans userType): {}", customRequest.getAuthorizationUri());
        return customRequest;
    }
}