package com.progetto.gestionale.service;

import java.util.List;

import com.progetto.gestionale.entity.Prodotto;

public interface ProdottiService {

    Prodotto creaProdotto(Prodotto prodotto);

    List<Prodotto> ottieniTuttiProdotti();
    
}
