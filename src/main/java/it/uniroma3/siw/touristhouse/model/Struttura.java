package it.uniroma3.siw.touristhouse.model;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class Struttura {

    private String nome = "Siw Touristhouse";
    private String descrizione = "Un'accogliente struttura nel cuore dell'urbe, facilmente raggiungibile con metro B e C .";
    private String indirizzo = "Via Roma 1, 00100 Roma (RM)";

    private String orarioCheckInInizio = "14:00";
    private String orarioCheckOut = "10:00";

    private String politicaCancellazione = "Cancellazione gratuita fino a 72 ore prima dell'arrivo.";

    private String email = "info@siwtouristhouse.it";
    private String telefono = "+39 06 1234567";
    private String whatsapp = "+39 333 1234567";

    private List<String> serviziStruttura = List.of(
            "Wi-Fi gratuito",
            "Colazione inclusa",
            "Aria condizionata",
            "Animali non ammessi");

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
