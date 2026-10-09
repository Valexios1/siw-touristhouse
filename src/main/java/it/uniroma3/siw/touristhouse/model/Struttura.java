package it.uniroma3.siw.touristhouse.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Struttura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    private String indirizzo;

    private String orarioCheckInInizio;

    private String orarioCheckInFine;

    private String orarioCheckOut;

    @Column(columnDefinition = "TEXT")
    private String politicaCancellazione;

    private String email;
    private String telefono;
    private String whatsapp;

    @ElementCollection
    private List<String> serviziStruttura;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getOrarioCheckInInizio() {
        return orarioCheckInInizio;
    }

    public void setOrarioCheckInInizio(String orarioCheckInInizio) {
        this.orarioCheckInInizio = orarioCheckInInizio;
    }

    public String getOrarioCheckInFine() {
        return orarioCheckInFine;
    }

    public void setOrarioCheckInFine(String orarioCheckInFine) {
        this.orarioCheckInFine = orarioCheckInFine;
    }

    public String getOrarioCheckOut() {
        return orarioCheckOut;
    }

    public void setOrarioCheckOut(String orarioCheckOut) {
        this.orarioCheckOut = orarioCheckOut;
    }

    public String getPoliticaCancellazione() {
        return politicaCancellazione;
    }

    public void setPoliticaCancellazione(String politicaCancellazione) {
        this.politicaCancellazione = politicaCancellazione;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public List<String> getServiziStruttura() {
        return serviziStruttura;
    }

    public void setServiziStruttura(List<String> serviziStruttura) {
        this.serviziStruttura = serviziStruttura;
    }
}