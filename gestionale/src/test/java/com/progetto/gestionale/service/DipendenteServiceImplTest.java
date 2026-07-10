package com.progetto.gestionale.service;

import com.progetto.gestionale.entity.Dipendente;
import com.progetto.gestionale.repository.DipendenteRepository;
import com.progetto.gestionale.service.impl.DipendenteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
 * ============================================================================
 * PIRAMIDE DEL TESTING — LIVELLO 1: UNIT TEST (la base della piramide)
 * ============================================================================
 *
 * COSA CARICA: niente. Nessun contesto Spring, nessun server, nessun database.
 * Solo la classe DipendenteServiceImpl istanziata a mano, come un normale
 * oggetto Java.
 *
 * COSA È REALE: la logica di business del service (il codice che vogliamo
 * verificare).
 *
 * COSA È FINTO: il repository. È un mock di Mockito: il database qui NON
 * esiste, il mock risponde ciò che decidiamo noi — per questo il test gira
 * in millisecondi e possiamo simulare con facilità anche i casi "scomodi"
 * (es. email già presente) senza preparare dati veri.
 *
 * PERCHÉ QUESTO TEST VIVE QUI E NON A UN ALTRO LIVELLO: la regola
 * "non possono esistere due dipendenti con la stessa email" è pura logica
 * del service. Per verificarla non serve né Spring né un database vero:
 * usarli renderebbe il test solo più lento, senza aumentare la fiducia
 * in QUESTA regola. La query findByEmail "vera" viene verificata al
 * livello 2 (integration test sul repository).
 * ============================================================================
 */
@ExtendWith(MockitoExtension.class)
class DipendenteServiceImplTest {

    @Mock
    private DipendenteRepository dipendenteRepository;

    private DipendenteServiceImpl dipendenteService;

    @BeforeEach
    void setUp() {
        // Istanziazione manuale: nessuna magia di Spring, solo un costruttore
        dipendenteService = new DipendenteServiceImpl(dipendenteRepository);
    }

    @Test
    void creaDipendente_emailGiaEsistente_lanciaIllegalArgumentException() {
        // Arrange: il mock finge che nel "database" esista già
        // un dipendente con quella email
        Dipendente giaPresente = nuovoDipendente("Luca", "Verdi", "luca.verdi@azienda.it");
        when(dipendenteRepository.findByEmail("luca.verdi@azienda.it"))
                .thenReturn(Optional.of(giaPresente));

        Dipendente duplicato = nuovoDipendente("Luca", "Verdi", "luca.verdi@azienda.it");

        // Act + Assert: la regola di business deve bloccare il duplicato
        assertThatThrownBy(() -> dipendenteService.creaDipendente(duplicato))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("email già esistente");

        // Assert: se la regola blocca, il salvataggio NON deve mai avvenire
        verify(dipendenteRepository, never()).save(any());
    }

    @Test
    void creaDipendente_emailNuova_salvaERestituisceIlDipendente() {
        // Arrange: il mock finge che l'email non sia ancora usata...
        when(dipendenteRepository.findByEmail("anna.rossi@azienda.it"))
                .thenReturn(Optional.empty());

        // ...e che il salvataggio restituisca il dipendente salvato
        Dipendente daCreare = nuovoDipendente("Anna", "Rossi", "anna.rossi@azienda.it");
        when(dipendenteRepository.save(daCreare)).thenReturn(daCreare);

        // Act
        Dipendente risultato = dipendenteService.creaDipendente(daCreare);

        // Assert: il service restituisce il dipendente e ha davvero
        // delegato il salvataggio al repository
        assertThat(risultato.getEmail()).isEqualTo("anna.rossi@azienda.it");
        assertThat(risultato.getNome()).isEqualTo("Anna");
        verify(dipendenteRepository).save(daCreare);
    }

    // Piccola factory per non ripetere i setter in ogni test
    private Dipendente nuovoDipendente(String nome, String cognome, String email) {
        Dipendente dipendente = new Dipendente();
        dipendente.setNome(nome);
        dipendente.setCognome(cognome);
        dipendente.setEmail(email);
        dipendente.setStipendio(1800.0);
        return dipendente;
    }
}
