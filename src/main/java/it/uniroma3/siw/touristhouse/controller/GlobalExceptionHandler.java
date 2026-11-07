package it.uniroma3.siw.touristhouse.controller;

import it.uniroma3.siw.touristhouse.exception.CameraNotFoundException;
import it.uniroma3.siw.touristhouse.exception.PrenotazioneNotFoundException;
import it.uniroma3.siw.touristhouse.exception.RecensioneNotFoundException;
import it.uniroma3.siw.touristhouse.exception.RegolaPrezzoNotFoundException;
import it.uniroma3.siw.touristhouse.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({ CameraNotFoundException.class, PrenotazioneNotFoundException.class,
            RegolaPrezzoNotFoundException.class, RecensioneNotFoundException.class,
            UserNotFoundException.class, NoResourceFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(Exception ex, Model model) {
        if (ex instanceof NoResourceFoundException) {
            model.addAttribute("messaggio", "Risorsa non trovata.");
        } else {
            model.addAttribute("messaggio", ex.getMessage());
        }
        return "error/404";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(IllegalArgumentException ex, Model model) {
        model.addAttribute("messaggio", ex.getMessage());
        return "error/400";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model) {
        logger.error("Errore non gestito", ex);
        model.addAttribute("messaggio", ex.getMessage());
        return "error/500";
    }
}
