package it.uniroma3.siw.touristhouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.touristhouse.model.Credentials;
import it.uniroma3.siw.touristhouse.model.User;
import it.uniroma3.siw.touristhouse.service.CameraService;
import it.uniroma3.siw.touristhouse.service.CredentialService;
import it.uniroma3.siw.touristhouse.service.ImmagineService;
import it.uniroma3.siw.touristhouse.service.RecensioneService;

@Controller
public class HomeController {

    @Autowired
    private CameraService cameraService;

    @Autowired
    private ImmagineService immagineService;

    @Autowired
    private RecensioneService recensioneService;

    @Autowired
    private CredentialService credentialService;

    @GetMapping("/")
    public String getHome(Model model) {
        model.addAttribute("camere", cameraService.findAll());
        model.addAttribute("fotoHome", immagineService.getImmagini("home"));
        model.addAttribute("recensioni", recensioneService.ultime5());

        User utente = utenteCorrente();
        Long utenteId = utente != null ? utente.getId() : null;
        model.addAttribute("utenteId", utenteId);
        model.addAttribute("puoRecensire", recensioneService.puoRecensire(utenteId));
        return "index";
    }

    private User utenteCorrente() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        Credentials credentials = credentialService.getCredentials(authentication.getName());
        return credentials != null ? credentials.getUser() : null;
    }
}
