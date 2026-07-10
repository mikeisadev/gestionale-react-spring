package com.progetto.gestionale.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * ============================================================================
 * PIRAMIDE DEL TESTING — LIVELLO 2: INTEGRATION TEST (il centro della piramide)
 * ============================================================================
 *
 * COSA CARICA: l'applicazione Spring INTERA (@SpringBootTest): controller,
 * service, repository, exception handling e database H2 (profilo "test").
 * L'unica cosa che manca è il server HTTP: MockMvc "recita" la parte del
 * client e consegna le richieste direttamente a Spring MVC, senza passare
 * da una porta di rete.
 *
 * COSA È REALE: qui Spring, la serializzazione JSON, la gestione delle
 * eccezioni e il database sono VERI; è più lento del livello 1 ma verifica
 * il CONTRATTO tra i pezzi: una regola di business che nasce nel service
 * deve arrivare al client come status 400 con un messaggio sensato.
 *
 * COSA È FINTO: solo il trasporto HTTP (nessun socket di rete).
 *
 * PERCHÉ QUESTO TEST VIVE QUI E NON A UN ALTRO LIVELLO: al livello 1
 * abbiamo già verificato che il service lancia IllegalArgumentException
 * per email duplicata. Ma chi garantisce che quell'eccezione diventi un
 * 400 con il messaggio giusto nel JSON di risposta? Quello è lavoro di
 * squadra tra controller, service e infrastruttura Spring: si può
 * verificare solo facendoli collaborare davvero.
 * ============================================================================
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DipendenteControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void creaDipendente_emailNuova_restituisce201EIlGetLoRitrova() throws Exception {
        // Arrange: il JSON che invierebbe il frontend
        String nuovoDipendente = """
                {
                    "nome": "Giulia",
                    "cognome": "Ferrari",
                    "email": "giulia.ferrari@azienda.it",
                    "stipendio": 2200.0
                }
                """;

        // Act + Assert: il POST attraversa controller -> service -> repository -> H2
        mockMvc.perform(post("/api/dipendenti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nuovoDipendente))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.email").value("giulia.ferrari@azienda.it"));

        // Assert: il GET della lista ritrova il dipendente appena creato
        mockMvc.perform(get("/api/dipendenti"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].email",
                        hasItem("giulia.ferrari@azienda.it")));
    }

    @Test
    void creaDipendente_emailDuplicata_restituisce400ConMessaggio() throws Exception {
        // Arrange: creo un primo dipendente con una certa email...
        String primoDipendente = """
                {
                    "nome": "Paolo",
                    "cognome": "Russo",
                    "email": "paolo.russo@azienda.it",
                    "stipendio": 1900.0
                }
                """;
        mockMvc.perform(post("/api/dipendenti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(primoDipendente))
                .andExpect(status().isCreated());

        // ...e preparo un secondo dipendente con la STESSA email
        String dipendenteDuplicato = """
                {
                    "nome": "Paola",
                    "cognome": "Russi",
                    "email": "paolo.russo@azienda.it",
                    "stipendio": 2000.0
                }
                """;

        // Act + Assert: la IllegalArgumentException lanciata dal service
        // deve risalire tutta la pila e diventare un 400 con ApiResponse.
        // È il contratto d'errore che il frontend si aspetta.
        mockMvc.perform(post("/api/dipendenti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dipendenteDuplicato))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("email già esistente"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }
}
