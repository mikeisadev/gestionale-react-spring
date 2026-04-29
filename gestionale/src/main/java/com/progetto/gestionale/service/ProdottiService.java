package com.progetto.gestionale.service;

import java.util.List;

import com.progetto.gestionale.entity.Prodotto;

import com.progetto.gestionale.dto.ProdottoDTO;

public interface ProdottiService {

    Prodotto creaProdotto(Prodotto prodotto);

    List<Prodotto> ottieniTuttiProdotti();
    
}
