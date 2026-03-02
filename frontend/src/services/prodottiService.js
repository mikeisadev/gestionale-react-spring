import axiosInstance from '../api/axiosInstance';

export const prodottiService = {

    // Recupera tutti i prodotti dal server
    getProdotti: () => axiosInstance.get('/api/prodotti'),

    // Invia un nuovo prodotto al server
    creaProdotto: (prodotto) => axiosInstance.post('/api/prodotti', prodotto),
}