package it.uniroma3.siw.touristhouse.exception;

public class RegolaPrezzoNotFoundException extends RuntimeException {

    public RegolaPrezzoNotFoundException(Long id) {
        super("RegolaPrezzo con id " + id + " non trovata.");
    }
}
