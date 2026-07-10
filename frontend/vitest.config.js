import { defineConfig } from 'vitest/config'
import react from '@vitejs/plugin-react'

/**
 * Configurazione usata SOLO dai test (npm run test).
 * La configurazione di build/dev resta in vite.config.js e non viene toccata.
 */
export default defineConfig({
  plugins: [react()],
  test: {
    // jsdom simula il DOM del browser dentro Node:
    // i componenti si montano davvero, ma senza aprire un browser
    environment: 'jsdom',
    // rende disponibili describe/it/expect senza import espliciti
    globals: true,
    // matcher extra (toBeInTheDocument, ...) caricati prima di ogni test
    setupFiles: './src/test/setup.js',
  },
})
