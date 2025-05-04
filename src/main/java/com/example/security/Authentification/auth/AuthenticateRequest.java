package com.example.security.Authentification.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Builder
public class AuthenticateRequest {
    @Email(message = "L'email n'est pas bien formaté")
    @NotEmpty(message = "L'email est obligatoire")
    @NotNull(message = "L'email est obligatoire")
    private String email;
    @NotEmpty(message = "Le mot de passe est obligatoire")
    @NotNull(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit comporter au moins 8 caractères")
    private String password;
}
