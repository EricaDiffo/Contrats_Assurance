package com.ericadiffo.contratsassuranceapi.service;

import com.ericadiffo.contratsassuranceapi.dto.GarantieRequestDTO;
import com.ericadiffo.contratsassuranceapi.dto.GarantieResponseDTO;
import com.ericadiffo.contratsassuranceapi.exception.ResourceNotFoundException;
import com.ericadiffo.contratsassuranceapi.models.Contrat;
import com.ericadiffo.contratsassuranceapi.models.Garantie;
import com.ericadiffo.contratsassuranceapi.repository.ContratRepository;
import com.ericadiffo.contratsassuranceapi.repository.GarantieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GarantieService{
    private final GarantieRepository garantieRepository;
    private final ContratRepository contratRepository;

    public GarantieResponseDTO create(GarantieRequestDTO requestDTO) {
        Contrat contrat = contratRepository.findById(requestDTO.getContratId())
                .orElseThrow(() -> new ResourceNotFoundException("Contrat introuvable avec l'id : " + requestDTO.getContratId()));

        Garantie garantie = Garantie.builder()
                .nom(requestDTO.getNom())
                .description(requestDTO.getDescription())
                .plafondCouverture(requestDTO.getPlafondCouverture())
                .franchise(requestDTO.getFranchise())
                .contrat(contrat)
                .build();

        Garantie savedGarantie = garantieRepository.save(garantie);
        return toDTO(savedGarantie);
    }

    @Transactional(readOnly = true)
    public GarantieResponseDTO findById(UUID id) {
        Garantie garantie = garantieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Garantie introuvable avec l'ID: " + id));
        return toDTO(garantie);
    }

    @Transactional(readOnly = true)
    public List<GarantieResponseDTO> findByContratId(UUID contratId) {
        return garantieRepository.findByContratId(contratId).stream()
                .map(this::toDTO)
                .toList();
    }

    public GarantieResponseDTO update(UUID id, GarantieRequestDTO requestDTO) {
        Garantie garantie = garantieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Garantie introuvable avec l'ID: " + id));

        garantie.setNom(requestDTO.getNom());
        garantie.setDescription(requestDTO.getDescription());
        garantie.setPlafondCouverture(requestDTO.getPlafondCouverture());
        garantie.setFranchise(requestDTO.getFranchise());

        return toDTO(garantie);
    }

    public void delete(UUID id){
        if (!garantieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Garantie introuvable avec l'ID: " + id);
        }
        garantieRepository.deleteById(id);
    }

    private GarantieResponseDTO toDTO(Garantie garantie){
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