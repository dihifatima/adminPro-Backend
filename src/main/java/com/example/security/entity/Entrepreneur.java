package com.example.security.entity;

import com.example.security.Authentification.user.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("Entrepreneur")// si tu utilises l'héritage

public class Entrepreneur extends User {
    private String cin;         // Nom de l'entreprise
    private String nomEntreprise;         // Nom de l'entreprise
    private String secteurActivite;       // Domaine d'activité
    private String registreCommerce;      // Numéro RC
    private String identifiantFiscal;     // Numéro IF
    private String typeEntreprise;        // SARL, SAS, Auto-entrepreneur
    private LocalDate dateCreation;       // Date de création de l'entreprise
    private String siegeSocial;           // Adresse du siège social
}

