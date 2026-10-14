package it.uniroma3.siw.touristhouse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
public class Camera {

    public enum Tipologia {
        SINGOLA, DOPPIA, MATRIMONIALE, TRIPLA
    }

    public enum Stato {
        ATTIVA, IN_MANUTENZIONE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Integer numeroCamera;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Tipologia tipologia;

    @NotNull
    @Column(nullable = false)
    private Integer capienzaMassima;

    @Positive
    private Integer superficieMq;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Float prezzoBaseNotte;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Stato statoAttuale;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroCamera() {
        return numeroCamera;
    }

    public void setNumeroCamera(Integer nomeNumero) {
        this.numeroCamera = nomeNumero;
    }

    public Tipologia getTipologia() {
        return tipologia;
    }

    public void setTipologia(Tipologia tipologia) {
        this.tipologia = tipologia;
    }

    public Integer getCapienzaMassima() {
        return capienzaMassima;
    }

    public void setCapienzaMassima(Integer capienzaMassima) {
        this.capienzaMassima = capienzaMassima;
    }

    public Integer getSuperficieMq() {
        return superficieMq;
    }

    public void setSuperficieMq(Integer superficieMq) {
        this.superficieMq = superficieMq;
    }

    public Float getPrezzoBaseNotte() {
        return prezzoBaseNotte;
    }

    public void setPrezzoBaseNotte(Float prezzoBaseNotte) {
        this.prezzoBaseNotte = prezzoBaseNotte;
    }

    public Stato getStatoAttuale() {
        return statoAttuale;
    }

    public void setStatoAttuale(Stato statoAttuale) {
        this.statoAttuale = statoAttuale;
    }

}