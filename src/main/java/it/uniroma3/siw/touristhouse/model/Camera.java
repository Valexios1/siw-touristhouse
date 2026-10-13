package it.uniroma3.siw.touristhouse.model;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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

    @NotBlank
    private Long id;

    @NotNull
    private String nomeNumero;

    @Enumerated(EnumType.STRING)
    private Tipologia tipologia;

    private Integer capienzaMassima;

    private String numeroLetti;

    private Integer superficieMq;

    private Float prezzoBaseNotte;

    @Enumerated(EnumType.STRING)
    private Stato statoAttuale;

    private List<String> dotazioniCamera;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeNumero() {
        return nomeNumero;
    }

    public void setNomeNumero(String nomeNumero) {
        this.nomeNumero = nomeNumero;
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

    public String getNumeroLetti() {
        return numeroLetti;
    }

    public void setNumeroLetti(String numeroLetti) {
        this.numeroLetti = numeroLetti;
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

    public List<String> getDotazioniCamera() {
        return dotazioniCamera;
    }

    public void setDotazioniCamera(List<String> dotazioniCamera) {
        this.dotazioniCamera = dotazioniCamera;
    }
}