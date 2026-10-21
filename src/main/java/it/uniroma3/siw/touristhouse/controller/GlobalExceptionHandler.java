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

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CameraNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCameraNotFound(CameraNotFoundException ex, Model model) {
        model.addAttribute("messaggio", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(PrenotazioneNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlePrenotazioneNotFound(PrenotazioneNotFoundException ex, Model model) {
        model.addAttribute("messaggio", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFound(UserNotFoundException ex, Model model) {
        model.addAttribute("messaggio", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(RegolaPrezzoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleRegolaPrezzoNotFound(RegolaPrezzoNotFoundException ex, Model model) {
        model.addAttribute("messaggio", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(RecensioneNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleRecensioneNotFound(RecensioneNotFoundException ex, Model model) {
        model.addAttribute("messaggio", ex.getMessage());
        return "error/404";
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model) {
        logger.error("Errore non gestito", ex);
        model.addAttribute("messaggio", ex.getMessage());
        return "error/500";
    }
}
