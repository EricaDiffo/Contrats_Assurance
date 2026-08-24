package com.ericadiffo.contratsassuranceapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.UUID;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarantieRequestDTO{
    @NotBlank(message = "Le nom de la garantie est obligatoire")
    private String nom;

    private String description;

    @NotNull(message = "Le plafond de couverture est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal plafondCouverture;

    @NotNull(message = "La franchise est obligatoire")
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal franchise;

    @NotNull(message = "L'identifiant du contrat est obligatoire")
    private UUID contratId;
}