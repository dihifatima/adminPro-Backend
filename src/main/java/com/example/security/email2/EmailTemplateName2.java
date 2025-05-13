package com.example.security.email2;

import lombok.Getter;

@Getter
public enum EmailTemplateName2 {
    EnvoyerContact("Envoyer_contact"); // Nom correct d'énumération

    private final String name;

    EmailTemplateName2(String name) {
        this.name = name;
    }
}