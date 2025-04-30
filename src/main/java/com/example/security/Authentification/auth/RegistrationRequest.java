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
    private boolean isEtudiant;
    @AssertTrue(message = "C'est soit un admin, soit un étudiant (pas les deux, ni aucun).")
    private boolean isExactlyOneRole() {
        return isAdmin ^ isEtudiant; // XOR : un seul des deux doit être vrai
    }
    @NotEmpty(message = "Firstname is mandatory")
    @NotNull(message = "Firstname is mandatory")
    private String firstname;
    @NotEmpty(message = "Lastname is mandatory")
    @NotNull(message = "Lastname is mandatory")
    private String lastname;
    @Email(message = "Email is not well formatted")
    @NotEmpty(message = "Email is mandatory")
    @NotNull(message = "Email is mandatory")
    private String email;
    @NotEmpty(message = "Password is mandatory")
    @NotNull(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String password;





}
