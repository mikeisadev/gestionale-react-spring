import { useEffect, useState } from "react";
import axios from "axios";

function Prodotti() {
  const [prodotti, inserisciProdotti] = useState([]);

  const [prodottoForm, inserisciProdottoForm] = useState({
    titolo: '',
    descrizione: '',
    prezzo: 0,
    quantita: 0
  });

  /**
   * INTERROGARE IL BACKEND.
   *
   * Dobbiamo fare una richiesta HTTP al Controller Prodotti.
   */
  useEffect(() => {
    console.log("Componente avviato");

    prendiProdottiDalBackend();
  }, []);

  /**
   * Funzione per ottenere i prodotti dal backend
   */
  function prendiProdottiDalBackend() {
    axios.get("http://127.0.0.1:8081/api/prodotti")
      .then(response => {
        console.log(response.data)

        inserisciProdotti(response.data)
      })
  }

  /**
   * Salvare un nuovo prodotto
   * 
   * @param {*} e 
   */
  function salvaNuovoProdotto(e) {
    e.preventDefault();

    console.log(prodottoForm)

    /**
     * Aggiungo un prodotto al database facendo una richiesta post
     */
    axios.post("http://127.0.0.1:8081/api/prodotti", prodottoForm)
      .then(response => {
        console.log("Prodotto aggiunto");

        prendiProdottiDalBackend();
      })
      .catch(error => {
        console.log(error);
      })
  }

  /**
   * Rileva l'input per ogni campo intercettando
   * l'oggetto "evento"
   * 
   * @param {*} e 
   */
  function gestisciInputForm(e) {
    console.log(e.target.value, e.target.name)

    inserisciProdottoForm({
      ...prodottoForm,
      [e.target.name]: e.target.value
    })
  }

  return (
    <>
      <h1 className="text-3xl font-semibold mb-[10px]">Prodotti</h1>

      <form onSubmit={salvaNuovoProdotto} className="mb-[20px] p-4 border rounded">
        <div className="mb-[10px]">
          <label>Titolo</label>
          <input name="titolo" onInput={gestisciInputForm} type="text" placeholder="Inserisci il titolo..." className="w-full p-2 border rounded"/>
        </div>

        <div className="mb-[10px]">
          <label>Descrizione</label>
          <textarea name="descrizione" onInput={gestisciInputForm} placeholder="Inserisci la descrizione..." className="w-full p-2 border rounded"></textarea>
        </div>
        
        <div className="mb-[10px]">
          <label>Prezzo</label>
          <input name="prezzo" onInput={gestisciInputForm} type="number" placeholder="Inserisci il prezzo..." className="w-full p-2 border rounded"/>
        </div>

        <div className="mb-[10px]">
          <label>Quantita</label>
          <input name="quantita" onInput={gestisciInputForm} type="number" placeholder="Inserisci la quantità..." className="w-full p-2 border rounded"/>
        </div>

        <button type="submit" className="bg-blue-500 text-white px-4 py-2 rounded">Aggiungi</button>
      </form>

      <h4 className="text-3xl font-semibold mb-[10px]">Lista prodotti</h4>
      
      <div className="space-y-3">
        {
          prodotti.map((prodotto, index) => {

              return (
                <div key={index} className="custom-card border rounded-md p-4">
                  <p className="font-medium">Titolo prodotto: {prodotto.titolo}</p>
                  <p className="text-sm text-gray-600">Descrizione prodotto: {prodotto.descrizione}</p>
                  <p className="font-semibold text-blue-600">Prezzo prodotto: {prodotto.prezzo}</p>

                  <div className="space-x-3 mt-[15px]">
                    <button className="bg-green-700 text-white px-4 py-2 rounded">Aggiorna</button>
                    <button className="bg-red-500 text-white px-4 py-2 rounded">Elimina</button>
                  </div>
                </div>
              )
          })
        }
      </div>
    </>
  );
}

export default Prodotti;
