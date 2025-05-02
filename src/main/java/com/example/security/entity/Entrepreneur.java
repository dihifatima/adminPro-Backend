package com.example.security.entity;

import com.example.security.Authentification.user.User;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Entrepreneur extends User {
    private String nomEntreprise;
    private String secteurActivite;
    private String registreCommerce;
    private String identifiantFiscal;
    private String typeEntreprise; // ex SARL SAS
}

