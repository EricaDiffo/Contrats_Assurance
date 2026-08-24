package com.ericadiffo.contratsassuranceapi.controller;

import com.ericadiffo.contratsassuranceapi.dto.ContratRequestDTO;
import com.ericadiffo.contratsassuranceapi.dto.ContratResponseDTO;
import com.ericadiffo.contratsassuranceapi.exception.ResourceNotFoundException;
import com.ericadiffo.contratsassuranceapi.models.Contrat;
import com.ericadiffo.contratsassuranceapi.service.ContratService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(ContratController.class)
class ContratControllerTest{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ContratService contratService;

    @Test
    void devrait_creer_un_contrat_et_retouner_201() throws Exception {
        UUID clientId = UUID.randomUUID();

        ContratRequestDTO requestDTO = ContratRequestDTO.builder()
                .numeroContrat("CTR-2026-001")
                .typeContrat("Habitation")
                .dateDebut(LocalDate.of(2026, 1, 1))
                .primeMensuelle(BigDecimal.valueOf(45.90))
                .clientId(clientId)
                .build();

        ContratResponseDTO responseDTO = ContratResponseDTO.builder()
                .id(UUID.randomUUID())
                .numeroContrat("CTR-2026-001")
                .typeContrat("Habitation")
                .dateDebut(LocalDate.of(2026, 1, 1))
                .statut(Contrat.StatutContrat.ACTIF)
                .primeMensuelle(BigDecimal.valueOf(45.90))
                .clientId(clientId)
                .garanties(List.of())
                .build();

        when(contratService.create(any(ContratRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/contrats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroContrat").value("CTR-2026-001"))
                .andExpect(jsonPath("$.statut").value("ACTIF"));
    }

    @Test
    void devrait_rejeter_un_contrat_sans_client_id() throws Exception {
        ContratRequestDTO requestDTO = ContratRequestDTO.builder()
                .numeroContrat("CTR-2026-002")
                .typeContrat("Auto")
                .dateDebut(LocalDate.of(2026, 1, 1))
                .primeMensuelle(BigDecimal.valueOf(60))
                .clientId(null)
                .build();

        mockMvc.perform(post("/api/contrats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void devrait_retourner_404_si_contrat_introuvable() throws Exception {
        UUID id = UUID.randomUUID();
        when(contratService.findById(id))
                .thenThrow(new ResourceNotFoundException("Contrat introuvable"));

        mockMvc.perform(get("/api/contrats/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void devrait_resilier_un_contrat_et_retourner_le_statut_resilie() throws Exception {
        UUID id = UUID.randomUUID();

        ContratResponseDTO responseDTO = ContratResponseDTO.builder()
                .id(id)
                .numeroContrat("CTR-2026-001")
                .statut(Contrat.StatutContrat.RESILIE)
                .dateFin(LocalDate.now())
                .garanties(List.of())
                .build();

        when(contratService.resign(id)).thenReturn(responseDTO);

        mockMvc.perform(patch("/api/contrats/" + id + "/resign"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("RESILIE"));
    }

    @Test
    void devrait_retourner_les_contrats_dun_client() throws Exception {
        UUID clientId = UUID.randomUUID();
        ContratResponseDTO contrat = ContratResponseDTO.builder()
                .id(UUID.randomUUID())
                .clientId(clientId)
                .garanties(List.of())
                .build();

        when(contratService.findByClientId(clientId)).thenReturn(List.of(contrat));

        mockMvc.perform(get("/api/contrats/client/" + clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clientId").value(clientId.toString()));
    }
}