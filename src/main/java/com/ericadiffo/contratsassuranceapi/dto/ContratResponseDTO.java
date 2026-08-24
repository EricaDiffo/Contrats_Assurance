package com.ericadiffo.contratsassuranceapi.dto;

import com.ericadiffo.contratsassuranceapi.models.Contrat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContratResponseDTO{
    private UUID id;
    private String numeroContrat;
    private String typeContrat;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Contrat.StatutContrat statut;
    private BigDecimal primeMensuelle;
    private UUID clientId;
    private String clientNomComplet;
    private List<GarantieResponseDTO> garanties;
}