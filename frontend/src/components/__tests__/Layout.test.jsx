/**
 * ============================================================================
 * PIRAMIDE DEL TESTING — FRONTEND, UNIT TEST (la base della piramide)
 * ============================================================================
 *
 * COSA CARICA: solo il componente Layout dentro un MemoryRouter
 * (un router "in memoria" che non tocca l'URL del browser).
 *
 * COSA È REALE: il componente e il DOM simulato.
 *
 * COSA È FINTO: qui non serve fingere nulla! Layout è un'unità SENZA I/O:
 * non chiama servizi, non fa fetch, non legge lo storage. È il test più
 * economico in assoluto: nessun mock da preparare, nessuna attesa
 * asincrona, nessuna dipendenza esterna — solo "renderizza e guarda".
 * Quando un'unità è pura come questa, il unit test è praticamente gratis:
 * un motivo in più per progettare componenti piccoli e senza effetti.
 *
 * PERCHÉ QUESTO TEST VIVE QUI E NON A UN ALTRO LIVELLO: la domanda a cui
 * risponde ("il menu offre i link giusti?") riguarda solo questo
 * componente. Salire di livello non aggiungerebbe alcuna fiducia in più.
 * ============================================================================
 */
import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { MemoryRouter } from 'react-router'

import Layout from '../Layout'

describe('Componente Layout', () => {

    it('render_menuDiNavigazione_contieneIQuattroLinkDelGestionale', () => {
        // Arrange + Act: il MemoryRouter serve solo perché Layout
        // usa <Link> e <Outlet> di react-router
        render(
            <MemoryRouter>
                <Layout />
            </MemoryRouter>
        )

        // Assert: ogni voce del menu è un link che punta alla rotta giusta.
        // Query per ruolo + testo visibile: la "Testing Library way"
        expect(screen.getByRole('link', { name: 'Prodotti' }))
            .toHaveAttribute('href', '/prodotti')
        expect(screen.getByRole('link', { name: 'Categorie' }))
            .toHaveAttribute('href', '/categorie')
        expect(screen.getByRole('link', { name: 'Dipendenti' }))
            .toHaveAttribute('href', '/dipendenti')
        expect(screen.getByRole('link', { name: 'Progetti' }))
            .toHaveAttribute('href', '/progetti')
    })
})
