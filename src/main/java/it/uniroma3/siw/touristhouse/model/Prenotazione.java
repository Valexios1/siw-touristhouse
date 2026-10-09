package it.uniroma3.siw.touristhouse.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Prenotazione {

    public enum Stato {
        IN_ATTESA_DI_PAGAMENTO,
        CONFERMATA,
        CHECKED_IN,
        CHECKED_OUT,
        ANNULLATA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codicePrenotazione;

    private LocalDate dataCheckIn;

    private LocalDate dataCheckOut;

    private Integer numeroAdulti;

    private Integer numeroBambini;

    private Float prezzoTotale;

    @Enumerated(EnumType.STRING)
    private Stato stato;

    @Column(columnDefinition = "TEXT")
    private String noteCliente;

    private LocalDateTime dataCreazione;

    @ManyToOne
    private Camera camera;

    @ManyToOne
    private Cliente cliente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodicePrenotazione() {
        return codicePrenotazione;
    }

    public void setCodicePrenotazione(String codicePrenotazione) {
        this.codicePrenotazione = codicePrenotazione;
    }

    public LocalDate getDataCheckIn() {
        return dataCheckIn;
    }

    public void setDataCheckIn(LocalDate dataCheckIn) {
        this.dataCheckIn = dataCheckIn;
    }

    public LocalDate getDataCheckOut() {
        return dataCheckOut;
    }

    public void setDataCheckOut(LocalDate dataCheckOut) {
        this.dataCheckOut = dataCheckOut;
    }

    public Integer getNumeroAdulti() {
        return numeroAdulti;
    }

    public void setNumeroAdulti(Integer numeroAdulti) {
        this.numeroAdulti = numeroAdulti;
    }

    public Integer getNumeroBambini() {
        return numeroBambini;
    }

    public void setNumeroBambini(Integer numeroBambini) {
        this.numeroBambini = numeroBambini;
    }

    public Float getPrezzoTotale() {
        return prezzoTotale;
    }

    public void setPrezzoTotale(Float prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }

    public Stato getStato() {
        return stato;
    }

    public void setStato(Stato stato) {
        this.stato = stato;
    }

    public String getNoteCliente() {
        return noteCliente;
    }

    public void setNoteCliente(String noteCliente) {
        this.noteCliente = noteCliente;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}