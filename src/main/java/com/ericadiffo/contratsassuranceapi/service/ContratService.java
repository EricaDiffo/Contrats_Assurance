package com.ericadiffo.contratsassuranceapi.service;


import com.ericadiffo.contratsassuranceapi.dto.ContratRequestDTO;
import com.ericadiffo.contratsassuranceapi.dto.ContratResponseDTO;
import com.ericadiffo.contratsassuranceapi.dto.GarantieResponseDTO;
import com.ericadiffo.contratsassuranceapi.exception.ResourceNotFoundException;
import com.ericadiffo.contratsassuranceapi.models.Client;
import com.ericadiffo.contratsassuranceapi.models.Contrat;
import com.ericadiffo.contratsassuranceapi.models.Garantie;
import com.ericadiffo.contratsassuranceapi.repository.ClientRepository;
import com.ericadiffo.contratsassuranceapi.repository.ContratRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ContratService{
    private final ContratRepository contratRepository;
    private  final ClientRepository clientRepository;

    public ContratResponseDTO create(ContratRequestDTO requestDTO){
        if (requestDTO.getDateFin() != null && requestDTO.getDateFin().isBefore(requestDTO.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin doit être postérieure à la date de début.");
        }

        Client client  = clientRepository.findById(requestDTO.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable avec l'ID: " + requestDTO.getClientId()));

        Contrat contrat = Contrat.builder()
                .numeroContrat(requestDTO.getNumeroContrat())
                .typeContat(requestDTO.getTypeContrat())
                .dateDebut(requestDTO.getDateDebut())
                .dateFin(requestDTO.getDateFin())
                .client(client)
                .build();
        Contrat savedContrat = contratRepository.save(contrat);
        return toDTO(savedContrat);
    }

    @Transactional(readOnly = true)
    public ContratResponseDTO findById(UUID id){
        Contrat contrat = contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat introuvable avec l'ID: " + id));
        return toDTO(contrat);
    }

    @Transactional(readOnly = true)
    public List<ContratResponseDTO> findByClientId(UUID clientId){
        return contratRepository.findByClientId(clientId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ContratResponseDTO resign(UUID id){
        Contrat contrat = contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat introuvable avec l'ID: " + id));
        contrat.setStatut(Contrat.StatutContrat.RESILIE);
        contrat.setDateFin(LocalDate.now());

        return toDTO(contrat);
    }

    public void delete(UUID id){
        if (!contratRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contrat introuvable avec l'ID: " + id);
        }
        contratRepository.deleteById(id);
    }

    private ContratResponseDTO toDTO(Contrat contrat) {
        List<GarantieResponseDTO> garantiesDTO = contrat.getGaranties().stream()
                .map(this::garantieToDTO)
                .collect(Collectors.toList());

        return ContratResponseDTO.builder()
                .id(contrat.getId())
                .numeroContrat(contrat.getNumeroContrat())
                .typeContrat(contrat.getTypeContat())
                .dateDebut(contrat.getDateDebut())
                .dateFin(contrat.getDateFin())
                .statut(contrat.getStatut())
                .primeMensuelle(contrat.getPrimeMensuelle())
                .clientId(contrat.getClient().getId())
                .clientNomComplet(contrat.getClient().getPrenom() + " " + contrat.getClient().getNom())
                .garanties(garantiesDTO)
                .build();
    }

    private GarantieResponseDTO garantieToDTO(Garantie garantie) {
        return GarantieResponseDTO.builder()
                .id(garantie.getId())
                .nom(garantie.getNom())
                .description(garantie.getDescription())
                .plafondCouverture(garantie.getPlafondCouverture())
                .franchise(garantie.getFranchise())
                .contratId(garantie.getContrat().getId())
                .build();
    }

}