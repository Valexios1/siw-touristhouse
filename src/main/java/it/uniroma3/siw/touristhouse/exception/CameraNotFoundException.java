package it.uniroma3.siw.touristhouse.exception;

public class CameraNotFoundException extends RuntimeException {

    public CameraNotFoundException(Long id) {
        super("Camera con id " + id + " non trovata.");
    }
}
