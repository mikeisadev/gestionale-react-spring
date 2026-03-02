import { useEffect, useState } from "react";
import axios from 'axios';

const Prodotti = () => {
    const [prodotti, inserisciProdotti] = useState([]);

    useEffect(() => {
        axios.get("http://localhost:8081/api/prodotti")
            .then(response => {
                console.log(response.data);

                inserisciProdotti(response.data)
            })
            .catch(error => console.error(error))

    }, []);

    return (
        <div>
            <div>Prodotti</div>
            {
                prodotti.map(prodotto => <div>{prodotto.titolo}</div>)
            }
        </div>
    );
}

export default Prodotti;