package com.ericadiffo.contratsassuranceapi.controller;

import com.ericadiffo.contratsassuranceapi.dto.ClientRequestDTO;
import com.ericadiffo.contratsassuranceapi.dto.ClientResponseDTO;
import com.ericadiffo.contratsassuranceapi.service.ClientService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClientService clientService;

    @Test
    void devrait_creer_un_client_et_retourner_201() throws Exception {
        ClientRequestDTO requestDTO = ClientRequestDTO.builder()
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@example.com")
                .telephone("0612345678")
                .dateNaissance(LocalDate.of(1990, 5, 15))
                .build();

        ClientResponseDTO responseDTO = ClientResponseDTO.builder()
                .id(UUID.randomUUID())
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@example.com")
                .telephone("0612345678")
                .dateNaissance(LocalDate.of(1990, 5, 15))
                .build();

        when(clientService.create(any(ClientRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Dupont"))
                .andExpect(jsonPath("$.email").value("jean.dupont@example.com"));
    }

    @Test
    void devrait_rejeter_un_client_avec_email_invalide() throws Exception {
        ClientRequestDTO requestDTO = ClientRequestDTO.builder()
                .nom("Dupont")
                .prenom("Jean")
                .email("pas-un-email")
                .telephone("0612345678")
                .dateNaissance(LocalDate.of(1990, 5, 15))
                .build();

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void devrait_retourner_tous_les_clients() throws Exception {
        ClientResponseDTO client1 = ClientResponseDTO.builder()
                .id(UUID.randomUUID()).nom("Dupont").prenom("Jean").build();

        when(clientService.findAll()).thenReturn(List.of(client1));

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Dupont"));
    }

    @Test
    void devrait_retourner_404_si_client_introuvable() throws Exception {
        UUID id = UUID.randomUUID();
        when(clientService.findById(id))
                .thenThrow(new com.ericadiffo.contratsassuranceapi.exception.ResourceNotFoundException("Client introuvable"));

        mockMvc.perform(get("/api/clients/" + id))
                .andExpect(status().isNotFound());
    }
}