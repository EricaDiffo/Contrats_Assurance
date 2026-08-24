package com.ericadiffo.contratsassuranceapi.controller;

import com.ericadiffo.contratsassuranceapi.dto.ClientRequestDTO;
import com.ericadiffo.contratsassuranceapi.dto.ClientResponseDTO;
import com.ericadiffo.contratsassuranceapi.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController{
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponseDTO> create(@Valid @RequestBody ClientRequestDTO requestDTO){
        ClientResponseDTO responseDTO = clientService.create(requestDTO);
        return ResponseEntity.status((HttpStatus.CREATED)).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> findById(@PathVariable UUID id){
        return ResponseEntity.ok(clientService.findById(id));
    }

    @GetMapping
    public  ResponseEntity<List<ClientResponseDTO>> findAll(){
        return ResponseEntity.ok(clientService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody ClientRequestDTO requestDTO){
        return ResponseEntity.ok(clientService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}