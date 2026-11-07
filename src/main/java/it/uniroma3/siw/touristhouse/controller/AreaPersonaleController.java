package it.uniroma3.siw.touristhouse.controller;

import it.uniroma3.siw.touristhouse.model.Credentials;
import it.uniroma3.siw.touristhouse.model.User;
import it.uniroma3.siw.touristhouse.service.CredentialService;
import it.uniroma3.siw.touristhouse.service.PrenotazioneService;
import it.uniroma3.siw.touristhouse.service.RecensioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AreaPersonaleController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private RecensioneService recensioneService;

    @GetMapping("/area-personale")
    public String getAreaPersonale(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Credentials credentials = credentialService.getCredentials(authentication.getName());
        User utente = credentials.getUser();

        model.addAttribute("utente", utente);
        model.addAttribute("prenotazioni", prenotazioneService.findByUtente(utente.getId()));
        model.addAttribute("recensioni", recensioneService.findByUtente(utente.getId()));
        model.addAttribute("puoRecensire", recensioneService.puoRecensire(utente.getId()));
        return "areaPersonale/lista";
    }
}
