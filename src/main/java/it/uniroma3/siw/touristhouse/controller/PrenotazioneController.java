package it.uniroma3.siw.touristhouse.controller;

import it.uniroma3.siw.touristhouse.model.Camera;
import it.uniroma3.siw.touristhouse.model.Prenotazione;
import it.uniroma3.siw.touristhouse.service.PrenotazioneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/prenotazioni/{id}")
    public String getCamera(@PathVariable Long id, Model model) {
        Prenotazione prenotazione = prenotazioneService.findById(id);
        model.addAttribute("prenotazione", prenotazione);
        model.addAttribute("camera", prenotazione.getCamera());
        model.addAttribute("utente", prenotazione.getUtente());
        return "prenotazione/dettaglio";
    }

    @GetMapping("/admin/nuovaprenotazione")
    public String showForm(Model model) {
        model.addAttribute("prenotazione", new Prenotazione());
        //model.addAttribute("camera", );
        //model.addAttribute("utente", );
        return "admin/nuovaprenotazione";
    }

    @PostMapping("/admin/nuovaprenotazione")
    public String save(@Valid @ModelAttribute("prenotazione") Prenotazione prenotazione,
                       BindingResult bindingResult, Model model) {
        //model.addAttribute("camera", );
        //model.addAttribute("utente", );
        if (bindingResult.hasErrors()) {
            return "admin/nuovaprenotazione.html";
        }
        this.prenotazioneService.savePrenotazione(prenotazione);
        return "redirect:/index";
    }
}