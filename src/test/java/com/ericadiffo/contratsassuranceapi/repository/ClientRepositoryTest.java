package com.ericadiffo.contratsassuranceapi.repository;

import com.ericadiffo.contratsassuranceapi.models.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class ClientRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void devrait_sauvegarder_et_retrouver_un_client_par_email() {
        Client client = Client.builder()
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@example.com")
                .telephone("0612345678")
                .dateNaissance(LocalDate.of(1990, 5, 15))
                .build();

        clientRepository.save(client);
        Optional<Client> resultat = clientRepository.findByEmail("jean.dupont@example.com");

        assertThat(resultat).isPresent();
        assertThat(resultat.get().getNom()).isEqualTo("Dupont");
        assertThat(resultat.get().getId()).isNotNull();
    }

    @Test
    void devrait_retourner_vide_si_email_inexistant() {
        Optional<Client> resultat = clientRepository.findByEmail("inexistant@example.com");

        assertThat(resultat).isEmpty();
    }
}