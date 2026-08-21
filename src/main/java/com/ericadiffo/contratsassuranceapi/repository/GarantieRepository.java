package com.ericadiffo.contratsassuranceapi.repository;

import com.ericadiffo.contratsassuranceapi.models.Garantie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GarantieRepository extends JpaRepository<Garantie, UUID> {
    List<Garantie> findByContratId(UUID contratId);
}