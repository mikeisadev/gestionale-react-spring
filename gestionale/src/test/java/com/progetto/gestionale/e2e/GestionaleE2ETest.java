package com.progetto.gestionale.e2e;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * ============================================================================
 * PIRAMIDE DEL TESTING — LIVELLO 3: TEST FUNZIONALE E2E (il vertice)
 * ============================================================================
 *
 * COSA CARICA: TUTTO. L'applicazione parte davvero, con un server Tomcat
 * su una porta casuale (webEnvironment = RANDOM_PORT) e il database H2
 * del profilo "test".
 *
 * COSA È REALE: tutto, rete compresa. Il test parla con l'applicazione
 * SOLO via HTTP, come farebbe il frontend React: non sa nulla di classi,
 * service o repository. Vede solo URL, JSON e status code — per questo
 * il corpo delle richieste e delle risposte è una semplice Map, non un DTO.
 *
 * COSA È FINTO: nulla (il database è H2 in-memory solo per non dipendere
 * dal MariaDB locale).
 *
 * PERCHÉ QUESTO TEST VIVE QUI E NON A UN ALTRO LIVELLO: non verifica una
 * regola o un contratto tra due pezzi, ma un VIAGGIO dell'utente attraverso
 * il sistema completo. È il test più lento e più costoso: per questo al
 * vertice della piramide ce ne sono pochi, scelti bene.
 *
 * NOTA PER IL CORSO: gli stessi scenari, scritti in Gherkin
 * (Given/When/Then), diventano i test funzionali di Cucumber.
 * Qui il Gherkin è "solo" nei commenti — il passo successivo del corso
 * è renderlo eseguibile.
 * ============================================================================
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
class GestionaleE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    // Il tipo generico della risposta JSON: un oggetto ApiResponse
    // visto "dall'esterno", cioè come semplice mappa chiave -> valore
    private static final ParameterizedTypeReference<Map<String, Object>> JSON =
            new ParameterizedTypeReference<>() {};

    @Test
    void viaggioUtente_creaDipendente_loRitrova_eIlDuplicatoVieneRifiutato() {
        // ------------------------------------------------------------------
        // GIVEN: l'applicazione è avviata su una porta reale
        //        con un database pulito
        // ------------------------------------------------------------------
        Map<String, Object> anna = Map.of(
                "nome", "Anna",
                "cognome", "Colombo",
                "email", "anna.colombo@azienda.it",
                "stipendio", 2500.0
        );

        // ------------------------------------------------------------------
        // WHEN: creo il dipendente "Anna" via POST /api/dipendenti
        // ------------------------------------------------------------------
        ResponseEntity<Map<String, Object>> rispostaCreazione = restTemplate.exchange(
                "/api/dipendenti", HttpMethod.POST, new HttpEntity<>(anna), JSON);

        // THEN: il server risponde 201 Created
        assertThat(rispostaCreazione.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // ------------------------------------------------------------------
        // THEN: GET /api/dipendenti include Anna nella lista
        // ------------------------------------------------------------------
        ResponseEntity<Map<String, Object>> rispostaLista = restTemplate.exchange(
                "/api/dipendenti", HttpMethod.GET, null, JSON);

        assertThat(rispostaLista.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(emailPresenti(rispostaLista)).contains("anna.colombo@azienda.it");

        // ------------------------------------------------------------------
        // WHEN: provo a ricreare "Anna" con la stessa email
        // ------------------------------------------------------------------
        ResponseEntity<Map<String, Object>> rispostaDuplicato = restTemplate.exchange(
                "/api/dipendenti", HttpMethod.POST, new HttpEntity<>(anna), JSON);

        // ------------------------------------------------------------------
        // THEN: ricevo 400 con il messaggio di errore...
        // ------------------------------------------------------------------
        assertThat(rispostaDuplicato.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(rispostaDuplicato.getBody().get("message"))
                .isEqualTo("email già esistente");

        // ------------------------------------------------------------------
        // THEN: ...e la lista contiene ancora UNA sola Anna
        // ------------------------------------------------------------------
        ResponseEntity<Map<String, Object>> listaFinale = restTemplate.exchange(
                "/api/dipendenti", HttpMethod.GET, null, JSON);

        long quanteAnna = emailPresenti(listaFinale).stream()
                .filter("anna.colombo@azienda.it"::equals)
                .count();
        assertThat(quanteAnna).isEqualTo(1);
    }

    // Estrae la lista delle email dal campo "data" dell'ApiResponse
    @SuppressWarnings("unchecked")
    private List<Object> emailPresenti(ResponseEntity<Map<String, Object>> risposta) {
        List<Map<String, Object>> dipendenti =
                (List<Map<String, Object>>) risposta.getBody().get("data");
        return dipendenti.stream()
                .map(d -> d.get("email"))
                .toList();
    }
}
