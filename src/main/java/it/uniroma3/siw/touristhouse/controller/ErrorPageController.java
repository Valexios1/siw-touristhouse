package it.uniroma3.siw.touristhouse.controller;

import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ErrorPageController implements ErrorController {

    @GetMapping("/error/403")
    public String accessDenied(Model model, HttpServletResponse response) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        model.addAttribute("messaggio", "Non hai i permessi necessari per accedere a questa risorsa.");
        return "error/403";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, HttpServletResponse response, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int statusCode = status != null ? Integer.parseInt(status.toString()) : 500;
        response.setStatus(statusCode);

        if (statusCode == HttpStatus.FORBIDDEN.value()) {
            model.addAttribute("messaggio", "Non hai i permessi necessari per accedere a questa risorsa.");
            return "error/403";
        }
        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            model.addAttribute("messaggio", "Risorsa non trovata.");
            return "error/404";
        }
        if (statusCode == HttpStatus.BAD_REQUEST.value()) {
            model.addAttribute("messaggio", "La richiesta inviata non è valida.");
            return "error/400";
        }
        return "error/500";
    }
}
