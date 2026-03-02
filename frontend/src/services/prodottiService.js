import axiosInstance from '../api/axiosInstance';

// Recupera tutti i prodotti dal server
export function getProdotti() {
    return axiosInstance.get('/api/prodotti');
}

// Invia un nuovo prodotto al server
export function creaProdotto(prodotto) {
    return axiosInstance.post('/api/prodotti', prodotto);
}
