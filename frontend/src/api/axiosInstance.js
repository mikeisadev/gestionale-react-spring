import axios from 'axios';

// Istanza di axios con l'URL base del backend pre-configurato.
// Tutte le chiamate useranno automaticamente questo indirizzo come prefisso.
const axiosInstance = axios.create({
    baseURL: 'http://localhost:8081'
});

export default axiosInstance;
