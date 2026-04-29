package com.progetto.gestionale.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.progetto.gestionale.service.ProdottiService;

import jakarta.transaction.Transactional;

import com.progetto.gestionale.entity.Prodotto;
import com.progetto.gestionale.repository.ProdottoRepository;

import com.progetto.gestionale.dto.ProdottoDTO;

@Service
public class ProdottoServiceImpl implements ProdottiService {
    
    private final ProdottoRepository prodottoRepository;

    public ProdottoServiceImpl(
        ProdottoRepository prodottoRepository
    ) {
        this.prodottoRepository = prodottoRepository;
    }

    public Prodotto creaProdotto(Prodotto prodotto) {
        return prodottoRepository.save(prodotto);
    }

    public List<Prodotto> ottieniTuttiProdotti() {
        return prodottoRepository.findAll();
    }

}
