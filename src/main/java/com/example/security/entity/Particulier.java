package com.example.security.entity;

import com.example.security.Authentification.user.User;
import jakarta.persistence.DiscriminatorValue;
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
@DiscriminatorValue("Particulier")
public class Particulier extends User {
    private String objetDemande;
    private String ville;
    private String cinScanUrl;
    private String documentPrincipalUrl;
    private String noteSupplementaire;
}
