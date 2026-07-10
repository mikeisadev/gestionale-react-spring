package com.progetto.gestionale.repository;

import com.progetto.gestionale.entity.Dipendente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * ============================================================================
 * PIRAMIDE DEL TESTING — LIVELLO 2: INTEGRATION TEST (il centro della piramide)
 * ============================================================================
 *
 * COSA CARICA: una "fetta" (slice) di Spring. @DataJpaTest avvia SOLO lo
 * strato JPA: entity, repository, Hibernate e un database H2 in-memory.
 * Niente controller, niente service, niente server HTTP.
 *
 * COSA È REALE: il repository generato da Spring Data, le query SQL che
 * produce, il mapping delle entity e il database (H2). La differenza col
 * livello 1 è tutta qui: al livello 1 il repository era un mock che
 * rispondeva a comando; qui Spring, Hibernate e il database sono VERI.
 *
 * COSA È FINTO: nulla — ma il database è in-memory, quindi veloce e usa
 * il profilo "test" (vedi application-test.properties), non il MariaDB
 * di produzione.
 *
 * PERCHÉ QUESTO TEST VIVE QUI E NON A UN ALTRO LIVELLO: findByEmail non è
 * codice scritto da noi, è una query DERIVATA dal nome del metodo. Un unit
 * test con mock non potrebbe mai dirci se Spring Data la traduce nella SQL
 * giusta: serve un database vero. È più lento del livello 1, ma verifica
 * il contratto tra il nostro codice e il database.
 *
 * Nota sul nome: il suffisso IT (Integration Test) è la convenzione Maven
 * per distinguere questi test dagli unit test.
 * ============================================================================
 */
@DataJpaTest
@ActiveProfiles("test")
class DipendenteRepositoryIT {

    @Autowired
    private DipendenteRepository dipendenteRepository;

    /*
     * TestEntityManager: il "collega di banco" del repository.
     * Lo usiamo per preparare i dati di test scrivendo direttamente
     * nel database, senza passare dal codice che stiamo testando.
     */
    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByEmail_dipendenteSalvato_loRitrova() {
        // Arrange: inserisco un dipendente direttamente nel database H2
        Dipendente anna = new Dipendente();
        anna.setNome("Anna");
        anna.setCognome("Bianchi");
        anna.setEmail("anna.bianchi@azienda.it");
        anna.setStipendio(2000.0);
        entityManager.persistAndFlush(anna);

        // Act: la query derivata viene eseguita DAVVERO contro H2
        Optional<Dipendente> trovato =
                dipendenteRepository.findByEmail("anna.bianchi@azienda.it");

        // Assert: il dipendente c'è ed è proprio Anna
        assertThat(trovato).isPresent();
        assertThat(trovato.get().getNome()).isEqualTo("Anna");
        assertThat(trovato.get().getCognome()).isEqualTo("Bianchi");
    }

    @Test
    void findByEmail_emailInesistente_restituisceOptionalVuoto() {
        // Arrange: il database contiene un solo dipendente, con un'altra email
        Dipendente marco = new Dipendente();
        marco.setNome("Marco");
        marco.setCognome("Neri");
        marco.setEmail("marco.neri@azienda.it");
        marco.setStipendio(2100.0);
        entityManager.persistAndFlush(marco);

        // Act
        Optional<Dipendente> trovato =
                dipendenteRepository.findByEmail("nessuno@azienda.it");

        // Assert: nessun risultato — la query non deve "inventare" match
        assertThat(trovato).isEmpty();
    }
}
