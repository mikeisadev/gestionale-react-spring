import { useEffect, useState } from "react";
import axios from 'axios';

const Prodotti = () => {
    /**
     * Qui abbiamo lo state che contiene la lista dei prodotti
     * che vado a richiamare sul server (vedi chiamata HTTP di tipo
     * GET dentro useEffect)
     */
    const [prodotti, inserisciProdotti] = useState([]);

    /**
     * Oggetto che nello state di React va a mappare i campi
     * del form
     */
    const [formProdotto, compilaFormProdotto] = useState({
        titolo: '',
        descrizione: '',
        prezzo: 0,
        quantitaMagazzino: 0,
        urlImmagine: ''
    });

    useEffect(() => {
        /**
         * Vado a effettuare una chiamata HTTP di tipo GET al
         * server per ottenere i prodotti
         */
        axios.get("http://localhost:8081/api/prodotti")
            .then(response => {
                console.log(response.data);

                inserisciProdotti(response.data)
            })
            .catch(error => console.error(error))

    }, []);

    /**
     * Funzione per gestire l'evento di battitura dei campi del form
     */
    function gestisciField(event) {
        
        compilaFormProdotto(prev => ({
            ...prev,
            [event.target.name]: event.target.value
        }));

        console.log(formProdotto);

        axios.post()
    }

    /**
     * Funzione per gestire l'invio del form
     */
    function gestisciAggiungiProdotto(event) {
        // Questo previene il caricamento della pagina, un comportamento
        // di default ogni volta che invio un form
        event.preventDefault();

        console.log(formProdotto);

        /**
         * Qui invio una richiesta di tipo post al server per aggiungere un prodotto.
         */
        axios.post("http://localhost:8081/api/prodotti", formProdotto)
            .then(response => {
                console.log(response)

                // Vado a svuotare i campi dopo l'invio al server
                compilaFormProdotto({
                    titolo: '',
                    descrizione: '',
                    prezzo: 0,
                    quantitaMagazzino: 0,
                    urlImmagine: ''
                })

                /**
                 * Richiamare il server per ottenere i dati freschi dal
                 * database
                 */
                axios.get("http://localhost:8081/api/prodotti")
                    .then(response => {
                        console.log(response.data);

                        inserisciProdotti(response.data)
                    })
                    .catch(error => console.error(error))
            })
            .catch(error => console.error(error))
    }

    return (
        <div>
            <h1>Prodotti</h1>

            <h2>Aggiungi nuovo prodotto</h2>

            <form onSubmit={gestisciAggiungiProdotto}>
                <div>
                    <label htmlFor="titolo">Titolo prodotto</label>
                    <input 
                        id="titolo"
                        type="text"
                        name="titolo"
                        placeholder="Inserisci il titolo del prodotto..."
                        className="form-input"
                        onChange={gestisciField}
                        value={formProdotto.titolo}
                    />
                </div>

                <div>
                    <label htmlFor="descrizione">Descrizione prodotto</label>
                    <textarea 
                        id="descrizione"
                        type="text"
                        name="descrizione"
                        placeholder="Inserisci la descrizione del prodotto"
                        className="form-input"
                        rows="5"
                        onChange={gestisciField}
                        value={formProdotto.descrizione}
                    ></textarea>
                </div>

                <div>
                    <label htmlFor="prezzo">Prezzo prodotto</label>
                    <input 
                        id="prezzo"
                        type="number"
                        name="prezzo"
                        min={0}
                        step={0.01}
                        placeholder="Inserisci il prezzo del prodotto..."
                        className="form-input"
                        onChange={gestisciField}
                        value={formProdotto.prezzo}
                    />
                </div>

                <div>
                    <label htmlFor="quantita_magazzino">Quantità in magazzino</label>
                    <input 
                        id="quantitaMagazzino"
                        type="number"
                        name="quantitaMagazzino"
                        min={0}
                        placeholder="Inserisci la quantità in magazzino del prodotto..."
                        className="form-input"
                        onChange={gestisciField}
                        value={formProdotto.quantitaMagazzino}
                    />
                </div>

                <div>
                    <label htmlFor="urlImmagine">URL immagine</label>
                    <input 
                        id="urlImmagine"
                        type="text"
                        name="urlImmagine"
                        placeholder="Inserisci l'url dell'immagine del prodotto..."
                        className="form-input"
                        onChange={gestisciField}
                        value={formProdotto.urlImmagine}
                    />
                </div>
                
                <div>
                    <input 
                        type="submit"
                        value="Aggiungi prodotto"
                        className="form-submit-btn"
                    />
                </div>
            </form>

            <h2>Lista prodotti</h2>
            {
                prodotti.map(prodotto => <div>{prodotto.titolo}</div>)
            }
        </div>
    );
}

export default Prodotti;