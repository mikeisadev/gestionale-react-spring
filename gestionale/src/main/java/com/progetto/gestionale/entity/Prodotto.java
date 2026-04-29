package com.progetto.gestionale.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "prodotti")
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String titolo;

    private String descrizione;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prezzo;

    @DecimalMin(value = "0.00", inclusive = false)
    @Column(nullable = true, precision = 10, scale = 2)
    private BigDecimal prezzoScontato;

    @Column(nullable = false)
    private Long quantita;

    @Column(nullable = true)
    private Boolean visibile;

    private LocalDateTime creatoIl;
    
    private LocalDateTime aggiornatoIl;

    public Prodotto(@NotBlank String titolo, String descrizione,
            @NotNull @DecimalMin(value = "0.00", inclusive = false) BigDecimal prezzo,
            @DecimalMin(value = "0.00", inclusive = false) BigDecimal prezzoScontato, Long quantita, Boolean visibile,
            LocalDateTime creatoIl, LocalDateTime aggiornatoIl) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.prezzoScontato = prezzoScontato;
        this.quantita = quantita;
        this.visibile = visibile;
        this.creatoIl = creatoIl;
        this.aggiornatoIl = aggiornatoIl;
    }

    public Prodotto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public BigDecimal getPrezzoScontato() {
        return prezzoScontato;
    }

    public void setPrezzoScontato(BigDecimal prezzoScontato) {
        this.prezzoScontato = prezzoScontato;
    }

    public Long getQuantita() {
        return quantita;
    }

    public void setQuantita(Long quantita) {
        this.quantita = quantita;
    }

    public Boolean getVisibile() {
        return visibile;
    }

    public void setVisibile(Boolean visibile) {
        this.visibile = visibile;
    }

    public LocalDateTime getCreatoIl() {
        return creatoIl;
    }

    public void setCreatoIl(LocalDateTime creatoIl) {
        this.creatoIl = creatoIl;
    }

    public LocalDateTime getAggiornatoIl() {
        return aggiornatoIl;
    }

    public void setAggiornatoIl(LocalDateTime aggiornatoIl) {
        this.aggiornatoIl = aggiornatoIl;
    }

    

    
}
