package com.ericadiffo.contratsassuranceapi.service;

import com.ericadiffo.contratsassuranceapi.dto.ClientRequestDTO;
import com.ericadiffo.contratsassuranceapi.dto.ClientResponseDTO;
import com.ericadiffo.contratsassuranceapi.exception.ResourceNotFoundException;
import com.ericadiffo.contratsassuranceapi.models.Client;
import com.ericadiffo.contratsassuranceapi.repository.ClientRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientService{
    private final ClientRepository clientRepository;

    public ClientResponseDTO create(ClientRequestDTO requestDTO) {
        if (clientRepository.existsByEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("Un client avec cet email existe déjà");
        }

        Client client = Client.builder()
                .nom(requestDTO.getNom())
                .prenom(requestDTO.getPrenom())
                .email(requestDTO.getEmail())
                .telephone(requestDTO.getTelephone())
                .dateNaissance(requestDTO.getDateNaissance())
                .build();

        Client savedClient = clientRepository.save(client);
        return toDTO(savedClient);
    }

    @Transactional(readOnly = true)
    public ClientResponseDTO findById(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable avec l'id : " + id));
        return toDTO(client);
    }

    @Transactional(readOnly = true)
    public List<ClientResponseDTO> findAll() {
        return clientRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ClientResponseDTO update(UUID id, ClientRequestDTO requestDTO){
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable avec l'id : " + id));
        // Update client properties
        client.setNom(requestDTO.getNom());
        client.setPrenom(requestDTO.getPrenom());
        client.setTelephone(requestDTO.getTelephone());
        client.setDateNaissance(requestDTO.getDateNaissance());

        return toDTO(client);
    }

    public void delete(UUID id){
        if(!clientRepository.existsById(id)){
            throw new ResourceNotFoundException("Client introuvable avec l'id : " + id);
        }
        clientRepository.deleteById(id);
    }

    private ClientResponseDTO toDTO(Client client){
        return ClientResponseDTO.builder()
                .id(client.getId())
                .nom(client.getNom())
                .prenom(client.getPrenom())
                .email(client.getEmail())
                .telephone(client.getTelephone())
                .dateNaissance(client.getDateNaissance())
                .build();
    }
}