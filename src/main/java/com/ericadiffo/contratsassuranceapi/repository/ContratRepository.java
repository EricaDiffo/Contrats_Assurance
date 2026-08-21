package com.ericadiffo.contratsassuranceapi.repository;

import com.ericadiffo.contratsassuranceapi.models.Contrat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContratRepository extends JpaRepository<Contrat, UUID> {
    Optional<Contrat> findByNumeroContrat(String numeroContrat);
    List<Contrat> findByClientId(UUID clientId);
    List<Contrat>findByStatut(Contrat.StatutContrat statut);
}