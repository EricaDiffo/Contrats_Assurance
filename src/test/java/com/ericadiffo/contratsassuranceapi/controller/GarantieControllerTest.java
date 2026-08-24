package com.ericadiffo.contratsassuranceapi.controller;

import com.ericadiffo.contratsassuranceapi.dto.GarantieRequestDTO;
import com.ericadiffo.contratsassuranceapi.dto.GarantieResponseDTO;
import com.ericadiffo.contratsassuranceapi.exception.ResourceNotFoundException;
import com.ericadiffo.contratsassuranceapi.service.GarantieService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GarantieController.class)
class GarantieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GarantieService garantieService;

    @Test
    void devrait_creer_une_garantie_et_retourner_201() throws Exception {
        UUID contratId = UUID.randomUUID();

        GarantieRequestDTO requestDTO = GarantieRequestDTO.builder()
                .nom("Dégât des eaux")
                .description("Couverture des dommages liés à l'eau")
                .plafondCouverture(BigDecimal.valueOf(10000))
                .franchise(BigDecimal.valueOf(150))
                .contratId(contratId)
                .build();

        GarantieResponseDTO responseDTO = GarantieResponseDTO.builder()
                .id(UUID.randomUUID())
                .nom("Dégât des eaux")
                .description("Couverture des dommages liés à l'eau")
                .plafondCouverture(BigDecimal.valueOf(10000))
                .franchise(BigDecimal.valueOf(150))
                .contratId(contratId)
                .build();

        when(garantieService.create(any(GarantieRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/garanties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Dégât des eaux"));
    }

    @Test
    void devrait_rejeter_une_garantie_sans_plafond() throws Exception {
        GarantieRequestDTO requestDTO = GarantieRequestDTO.builder()
                .nom("Vol")
                .plafondCouverture(null)
                .franchise(BigDecimal.valueOf(100))
                .contratId(UUID.randomUUID())
                .build();

        mockMvc.perform(post("/api/garanties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void devrait_retourner_404_si_garantie_introuvable() throws Exception {
        UUID id = UUID.randomUUID();
        when(garantieService.findById(id))
                .thenThrow(new ResourceNotFoundException("Garantie introuvable"));

        mockMvc.perform(get("/api/garanties/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void devrait_retourner_les_garanties_dun_contrat() throws Exception {
        UUID contratId = UUID.randomUUID();
        GarantieResponseDTO garantie = GarantieResponseDTO.builder()
                .id(UUID.randomUUID())
                .nom("Incendie")
                .contratId(contratId)
                .build();

        when(garantieService.findByContratId(contratId)).thenReturn(List.of(garantie));

        mockMvc.perform(get("/api/garanties/contrat/" + contratId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Incendie"));
    }

    @Test
    void devrait_supprimer_une_garantie_et_retourner_204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/garanties/" + id))
                .andExpect(status().isNoContent());
    }
}