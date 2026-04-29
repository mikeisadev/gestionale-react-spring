package com.progetto.gestionale.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.progetto.gestionale.service.ProdottiService;

import com.progetto.gestionale.entity.Prodotto;

import com.progetto.gestionale.dto.ProdottoDTO;

@RestController
@RequestMapping("/api/prodotti")
@CrossOrigin(origins = {"http://localhost:5173/"})
public class ProdottoController {

    @Autowired
    ProdottiService prodottoService;

    /**
     * Definiamo il metodo per ottenere tutti i prodotti
     */
    @GetMapping
    public ResponseEntity<List<Prodotto>> ottieniProdotti() {
        List<Prodotto> prodotti = prodottoService.ottieniTuttiProdotti();

        return ResponseEntity.ok(prodotti);
    }

    /**
     * Metodo per creare un prodotto
     */
    @PostMapping
    public ResponseEntity<Prodotto> creaProdotto(@RequestBody Prodotto prodotto) {
        Prodotto nuovoProdotto = prodottoService.creaProdotto(prodotto);

        return ResponseEntity.ok(nuovoProdotto);
    }

    /**
     * Metodo per eliminare un prodotto
     */

    /**
     * Metodo per aggiornare un prodotto
     */
    
}
