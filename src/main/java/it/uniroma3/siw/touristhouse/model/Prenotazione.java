package it.uniroma3.siw.touristhouse.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Prenotazione {

    public enum Stato {
        IN_ATTESA_DI_PAGAMENTO,
        CONFERMATA,
        CHECKED_IN,
        CHECKED_OUT
    }

    /**
     * Stato del pagamento della prenotazione. Modificabile solo dall'amministratore.
     */
    public enum StatoPagamento {
        IN_SOSPESO,
        COMPLETATO,
        FALLITO,
        RIMBORSATO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @FutureOrPresent
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(nullable = false)
    private LocalDate dataCheckIn;

    @NotNull
    @Future
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(nullable = false)
    private LocalDate dataCheckOut;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer numeroAdulti;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Integer numeroBambini;

    private Float prezzoTotale;

    @Enumerated(EnumType.STRING)
    private Stato stato;

    @Enumerated(EnumType.STRING)
    private StatoPagamento statoPagamento = StatoPagamento.IN_SOSPESO;

    @Column(columnDefinition = "TEXT")
    private String noteCliente;


    @ManyToOne
    private Camera camera;

    @ManyToOne
    private User utente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public StatoPagamento getStatoPagamento() {
        return statoPagamento;
    }

    public void setStatoPagamento(StatoPagamento statoPagamento) {
        this.statoPagamento = statoPagamento;
    }

    public String getNoteCliente() {
        return noteCliente;
    }

    public void setNoteCliente(String noteCliente) {
        this.noteCliente = noteCliente;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public User getUtente() {
        return utente;
    }

    public void setUtente(User utente) {
        this.utente = utente;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Prenotazione that = (Prenotazione) o;
        return Objects.equals(id, that.id) && Objects.equals(camera, that.camera) && Objects.equals(utente, that.utente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, camera, utente);
    }
}
