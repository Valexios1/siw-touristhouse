package it.uniroma3.siw.touristhouse.exception;

public class PrenotazioneNotFoundException extends RuntimeException {

    public PrenotazioneNotFoundException(Long id) {
        super("Prenotazione con id " + id + " non trovata.");
    }
}
