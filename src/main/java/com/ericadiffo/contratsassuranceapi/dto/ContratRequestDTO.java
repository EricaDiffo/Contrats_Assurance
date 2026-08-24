package com.ericadiffo.contratsassuranceapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContratRequestDTO{
    @NotBlank(message = "Le numéro de contrat est obligatoire")
    private String numeroContrat;

    @NotBlank(message = "Le type de contrat est obligatoire")
    private String typeContrat;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    private LocalDate dateFin;

    @NotNull(message = "La prime mensuelle est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "La prime doit être positive")
    private BigDecimal primeMensuelle;

    @NotNull(message = "L'identifiant du client est obligatoire")
    private UUID clientId;
}