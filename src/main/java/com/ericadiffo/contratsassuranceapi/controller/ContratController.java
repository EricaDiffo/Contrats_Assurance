package com.ericadiffo.contratsassuranceapi.controller;

import com.ericadiffo.contratsassuranceapi.dto.ContratRequestDTO;
import com.ericadiffo.contratsassuranceapi.dto.ContratResponseDTO;
import com.ericadiffo.contratsassuranceapi.service.ContratService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/contrats")
@RequiredArgsConstructor
public class ContratController{
    private final ContratService contratService;

    @PostMapping
    public ResponseEntity<ContratResponseDTO> create(@Valid @RequestBody ContratRequestDTO requestDTO) {
        ContratResponseDTO response = contratService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(contratService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ContratResponseDTO>> findAll() {
        return ResponseEntity.ok(contratService.findAll());
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ContratResponseDTO>> findByClientId(@PathVariable UUID clientId) {
        return ResponseEntity.ok(contratService.findByClientId(clientId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContratResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody ContratRequestDTO requestDTO) {
        return ResponseEntity.ok(contratService.update(id, requestDTO));
    }

    @PatchMapping("/{id}/resign")
    public ResponseEntity<ContratResponseDTO> resign(@PathVariable UUID id) {
        return ResponseEntity.ok(contratService.resign(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        contratService.delete(id);
        return ResponseEntity.noContent().build();
    }
}