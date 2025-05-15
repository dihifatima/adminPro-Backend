package com.example.security.Authentification.auth;


import jakarta.persistence.Entity;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {

    private boolean isAdmin;
    private boolean isAdminSecondaire;
    private boolean isEtudiant;
    private boolean isEntrepreneur;
    private boolean isParticulier;
    private boolean isPorteVisa;

    @AssertTrue(message = "C'est soit un admin, soit un étudiant ou un des autre .")
    private boolean isExactlyOneRole() {
        int count = 0;
        if (isAdmin) count++;
        if (isAdminSecondaire) count++;
        if (isEtudiant) count++;
        if (isEntrepreneur) count++;
        if (isParticulier) count++;
        if (isPorteVisa) count++;
        return count == 1;
    }
    @NotEmpty(message = "Le prénom est obligatoire")
    private String firstname;
    @NotEmpty(message = "Le nom de famille est obligatoire")
    private String lastname;
    @Email(message = "L'email n'est pas bien formaté")
    @NotEmpty(message = "L'email est obligatoire")
    private String email;
    @NotEmpty(message = "Le numéro de téléphone est obligatoire")
    private String telephone;
    @NotEmpty(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit comporter au moins 8 caractères")
    private String password;
    @AssertTrue(message = "Vous devez accepter les conditions d'utilisation et la politique de confidentialité")
    private boolean consentGDPR;





}
