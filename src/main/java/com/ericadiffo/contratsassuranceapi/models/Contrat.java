package com.ericadiffo.contratsassuranceapi.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "contrats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Contrat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Le numero de contrat est obligatoire")
    @Column(nullable = false, unique = true)
    private String numeroContrat;

    @NotBlank(message = "Le type de contrat est obligatoire")
    private String typeContat;

    @NotNull(message = "La date de debut est obligatoire")
    private LocalDate dateDebut;

    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatutContrat statut = StatutContrat.ACTIF;

    @NotNull(message = "La prime mensuelle est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "La prime doit être positive")
    private BigDecimal primeMensuelle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "contrat", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Garantie> garanties = new ArrayList<>();

    public enum StatutContrat {
        ACTIF, RESILIE, EXPIRE, SUSPENDU
    }
}