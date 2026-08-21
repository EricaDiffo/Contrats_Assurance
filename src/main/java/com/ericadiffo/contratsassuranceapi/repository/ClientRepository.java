package com.ericadiffo.contratsassuranceapi.repository;

import com.ericadiffo.contratsassuranceapi.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    Optional<Client> findByEmail(String email);
    boolean existsByEmail(String email);
}