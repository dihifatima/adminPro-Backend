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
@DiscriminatorValue("PorteVisa")

public class PorteVisa extends User {
    private String destinationVisa;
    private String typeVisa;
    private String motif;
    private String passportScanUrl;
    private String justificatifDomicileUrl;
    private String releveBancaireUrl;
}
