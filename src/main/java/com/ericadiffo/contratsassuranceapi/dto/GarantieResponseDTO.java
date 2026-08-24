package com.ericadiffo.contratsassuranceapi.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarantieResponseDTO{
    private UUID id;
    private String nom;
    private String description;
    private BigDecimal plafondCouverture;
    private BigDecimal franchise;
    private UUID contratId;
}