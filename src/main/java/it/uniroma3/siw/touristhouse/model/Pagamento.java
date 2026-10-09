package it.uniroma3.siw.touristhouse.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Pagamento {

    public enum Metodo {
        CARTA_DI_CREDITO,
        PAYPAL,
        BONIFICO_BANCARIO,
        CONTANTI
    }

    public enum Stato {
        IN_SOSPESO,
        COMPLETATO,
        FALLITO,
        RIMBORSATO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float importo;

    private LocalDateTime dataTransazione;

    @Enumerated(EnumType.STRING)
    private Metodo metodo;

    @Enumerated(EnumType.STRING)
    private Stato stato;

    private String riferimentoTransazioneEsterna;

    @ManyToOne
    private Prenotazione prenotazione;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getImporto() {
        return importo;
    }

    public void setImporto(Float importo) {
        this.importo = importo;
    }

    public LocalDateTime getDataTransazione() {
        return dataTransazione;
    }

    public void setDataTransazione(LocalDateTime dataTransazione) {
        this.dataTransazione = dataTransazione;
    }

    public Metodo getMetodo() {
        return metodo;
    }

    public void setMetodo(Metodo metodo) {
        this.metodo = metodo;
    }

    public Stato getStato() {
        return stato;
    }

    public void setStato(Stato stato) {
        this.stato = stato;
    }

    public String getRiferimentoTransazioneEsterna() {
        return riferimentoTransazioneEsterna;
    }

    public void setRiferimentoTransazioneEsterna(String riferimentoTransazioneEsterna) {
        this.riferimentoTransazioneEsterna = riferimentoTransazioneEsterna;
    }

    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }
}