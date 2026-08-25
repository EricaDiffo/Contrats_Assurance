package com.ericadiffo.contratsassuranceapi.dto;

import com.ericadiffo.contratsassuranceapi.models.Utilisateur;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDTO {

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String motDePasse;

    @NotNull(message = "Le rôle est obligatoire")
    private Utilisateur.Role role;
}