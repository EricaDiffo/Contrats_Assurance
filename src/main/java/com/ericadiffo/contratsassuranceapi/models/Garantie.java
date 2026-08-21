package com.ericadiffo.contratsassuranceapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "garanties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Garantie{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Le nom de la garantie est obligatoire")
    @Column(nullable = false)
    private String nom;

    private String description;

    @NotNull(message = "Le plafond de couverture est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal plafondCouverture;

    @NotNull(message = "La franchise est obligatoire")
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal franchise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrat_id", nullable = false)
    private Contrat contrat;
}