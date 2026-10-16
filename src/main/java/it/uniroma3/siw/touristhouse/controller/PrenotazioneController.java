package it.uniroma3.siw.touristhouse.controller;

import it.uniroma3.siw.touristhouse.model.Camera;
import it.uniroma3.siw.touristhouse.model.Credentials;
import it.uniroma3.siw.touristhouse.model.Prenotazione;
import it.uniroma3.siw.touristhouse.service.CameraService;
import it.uniroma3.siw.touristhouse.service.CredentialService;
import it.uniroma3.siw.touristhouse.service.PrenotazioneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    @Autowired
    private CameraService cameraService;

    @Autowired
    private CredentialService credentialService;

    @GetMapping("/prenotazioni")
    public String getPrenotazioni(Model model) {
        model.addAttribute("prenotazioni", prenotazioneService.findAll());
        return "prenotazione/lista";
    }

    @GetMapping("/prenotazioni/{id}")
    public String getCamera(@PathVariable Long id, Model model) {
        Prenotazione prenotazione = prenotazioneService.findById(id);
        model.addAttribute("prenotazione", prenotazione);
        model.addAttribute("camera", prenotazione.getCamera());
        model.addAttribute("utente", prenotazione.getUtente());
        return "prenotazione/dettaglio";
    }

    @GetMapping("/prenotazioni/nuovaprenotazione")
    public String showForm(Model model) {
        model.addAttribute("prenotazione", new Prenotazione());
        model.addAttribute("camere", cameraService.findAll());
        model.addAttribute("stati", Prenotazione.Stato.values());
        return "prenotazione/nuovaprenotazione";
    }

    @PostMapping("/prenotazioni/nuovaprenotazione")
    public String save(@Valid @ModelAttribute("prenotazione") Prenotazione prenotazione,
                       BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("camere", cameraService.findAll());
            model.addAttribute("stati", Prenotazione.Stato.values());
            return "prenotazione/nuovaprenotazione";
        }

        Camera camera = cameraService.findById(prenotazione.getCamera().getId());
        prenotazione.setCamera(camera);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Credentials credentials = credentialService.getCredentials(authentication.getName());
        prenotazione.setUtente(credentials.getUser());

        this.prenotazioneService.savePrenotazione(prenotazione);
        return "redirect:/";
    }
}