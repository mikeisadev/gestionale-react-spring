package com.progetto.gestionale.service;

import com.progetto.gestionale.entity.Prodotto;
import com.progetto.gestionale.repository.ProdottoRepository;
import com.progetto.gestionale.service.impl.ProdottoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/*
 * ============================================================================
 * PIRAMIDE DEL TESTING — LIVELLO 1: UNIT TEST (la base della piramide)
 * ============================================================================
 *
 * COSA CARICA: niente. Nessun contesto Spring, nessun database, nessun server.
 *
 * COSA È REALE: ProdottoServiceImpl, cioè la logica che vogliamo verificare.
 *
 * COSA È FINTO: ProdottoRepository. Il database qui NON esiste: il mock
 * risponde ciò che decidiamo noi — per questo il test gira in millisecondi.
 * Simulare un "id inesistente" costa una riga di setup del mock, invece di
 * dover garantire che un database vero sia vuoto.
 *
 * PERCHÉ QUESTO TEST VIVE QUI E NON A UN ALTRO LIVELLO: stiamo verificando
 * DECISIONI del service (lanciare un'eccezione, aggiornare un campo prima di
 * salvare), non l'accesso ai dati. Sono comportamenti definiti interamente
 * dentro la classe: il livello più economico della piramide basta e avanza.
 * ============================================================================
 */
@ExtendWith(MockitoExtension.class)
class ProdottoServiceImplTest {

    @Mock
    private ProdottoRepository prodottoRepository;

    private ProdottoServiceImpl prodottoService;

    @BeforeEach
    void setUp() {
        prodottoService = new ProdottoServiceImpl(prodottoRepository);
    }

    @Test
    void trovaProdottoPerId_idInesistente_lanciaEccezioneConMessaggio() {
        // Arrange: il mock risponde "nessun prodotto" per l'id 99
        when(prodottoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert: il service traduce l'assenza in un'eccezione parlante
        assertThatThrownBy(() -> prodottoService.trovaProdottoPerId(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Prodotto non trovato");
    }

    @Test
    void aggiornaProdotto_prodottoEsistente_aggiornaIlTitoloESalva() {
        // Arrange: nel "database" finto esiste un prodotto con un vecchio titolo
        Prodotto esistente = new Prodotto();
        esistente.setTitolo("Tastiera economica");
        when(prodottoRepository.findById(1L)).thenReturn(Optional.of(esistente));

        // save restituisce ciò che riceve, come farebbe il repository vero
        when(prodottoRepository.save(esistente)).thenReturn(esistente);

        Prodotto dettagliNuovi = new Prodotto();
        dettagliNuovi.setTitolo("Tastiera meccanica");

        // Act
        Prodotto risultato = prodottoService.aggiornaProdotto(1L, dettagliNuovi);

        // Assert: il titolo è stato aggiornato E l'oggetto salvato
        // è proprio quello con il titolo nuovo
        assertThat(risultato.getTitolo()).isEqualTo("Tastiera meccanica");

        ArgumentCaptor<Prodotto> salvato = ArgumentCaptor.forClass(Prodotto.class);
        verify(prodottoRepository).save(salvato.capture());
        assertThat(salvato.getValue().getTitolo()).isEqualTo("Tastiera meccanica");
    }
}
