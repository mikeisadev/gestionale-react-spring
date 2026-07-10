/**
 * ============================================================================
 * PIRAMIDE DEL TESTING — FRONTEND, UNIT TEST (la base della piramide)
 * ============================================================================
 *
 * COSA CARICA: solo il componente Prodotti, montato in un DOM simulato
 * (jsdom). Niente browser, niente server, niente rete.
 *
 * COSA È REALE: il componente React (render, state, gestione del form)
 * e il DOM su cui facciamo le query, come farebbe un utente.
 *
 * COSA È FINTO: il backend qui NON esiste: il mock del service
 * (vi.mock su prodottiService) è l'equivalente frontend del mock del
 * repository nei test del backend — stesso principio, altra metà
 * dell'applicazione. Decidiamo noi cosa "risponde il server", quindi il
 * test è veloce, ripetibile e non dipende da Spring Boot acceso.
 *
 * PERCHÉ QUESTO TEST VIVE QUI E NON A UN ALTRO LIVELLO: verifichiamo il
 * comportamento del componente (mostra i dati, invia il form), non il
 * dialogo reale con il server. Quel dialogo è già coperto dall'altra
 * parte della piramide: i test E2E del backend.
 * ============================================================================
 */
import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { describe, it, expect, vi, beforeEach } from 'vitest'

import Prodotti from '../Prodotti'
import { prodottiService } from '../../services/prodottiService'

// Sostituisce l'intero modulo del service con una versione finta:
// da qui in poi il componente parlerà con questi mock, non con axios
vi.mock('../../services/prodottiService', () => ({
    prodottiService: {
        getProdotti: vi.fn(),
        creaProdotto: vi.fn(),
    },
}))

describe('Pagina Prodotti', () => {

    beforeEach(() => {
        // Arrange comune: il "server" finto risponde con 2 prodotti
        vi.clearAllMocks()
        prodottiService.getProdotti.mockResolvedValue({
            data: [
                { id: 1, titolo: 'Trapano avvitatore' },
                { id: 2, titolo: 'Sega circolare' },
            ],
        })
        prodottiService.creaProdotto.mockResolvedValue({ data: { id: 3 } })
    })

    it('alMount_serviceRestituisceDueProdotti_mostraIDueTitoli', async () => {
        // Act: monto la pagina (lo useEffect chiama il service mockato)
        render(<Prodotti />)

        // Assert: i due titoli finti diventano visibili all'utente
        expect(await screen.findByText('Trapano avvitatore')).toBeInTheDocument()
        expect(await screen.findByText('Sega circolare')).toBeInTheDocument()
        expect(prodottiService.getProdotti).toHaveBeenCalledTimes(1)
    })

    it('invioForm_campiCompilati_chiamaCreaProdottoConIDatiInseriti', async () => {
        // Arrange: monto la pagina e preparo l'utente simulato
        const utente = userEvent.setup()
        render(<Prodotti />)

        // Act: compilo i campi come farebbe un utente (query per label,
        // cioè quello che l'utente legge davvero sullo schermo)
        await utente.type(
            screen.getByLabelText('Titolo prodotto'),
            'Martello demolitore'
        )
        await utente.type(
            screen.getByLabelText('Descrizione prodotto'),
            'Martello per cantiere'
        )
        // nota: un input type="number" normalizza il valore (149.90 -> 149.9)
        await utente.type(screen.getByLabelText('Prezzo prodotto'), '149.9')

        // ...e invio il form
        await utente.click(
            screen.getByRole('button', { name: 'Aggiungi prodotto' })
        )

        // Assert: il service riceve proprio i dati digitati nel form
        // (i value degli input HTML sono stringhe: il test lo documenta)
        await waitFor(() => {
            expect(prodottiService.creaProdotto).toHaveBeenCalledWith(
                expect.objectContaining({
                    titolo: 'Martello demolitore',
                    descrizione: 'Martello per cantiere',
                    prezzo: '149.9',
                })
            )
        })
    })
})
