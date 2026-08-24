package com.ericadiffo.contratsassuranceapi.controller;

import com.ericadiffo.contratsassuranceapi.dto.GarantieRequestDTO;
import com.ericadiffo.contratsassuranceapi.dto.GarantieResponseDTO;
import com.ericadiffo.contratsassuranceapi.service.GarantieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/garanties")
@RequiredArgsConstructor
public class GarantieController {

    private final GarantieService garantieService;

    @PostMapping
    public ResponseEntity<GarantieResponseDTO> create(@Valid @RequestBody GarantieRequestDTO requestDTO) {
        GarantieResponseDTO response = garantieService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GarantieResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(garantieService.findById(id));
    }

    @GetMapping("/contrat/{contratId}")
    public ResponseEntity<List<GarantieResponseDTO>> findByContratId(@PathVariable UUID contratId) {
        return ResponseEntity.ok(garantieService.findByContratId(contratId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GarantieResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody GarantieRequestDTO requestDTO) {
        return ResponseEntity.ok(garantieService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        garantieService.delete(id);
        return ResponseEntity.noContent().build();
    }
}