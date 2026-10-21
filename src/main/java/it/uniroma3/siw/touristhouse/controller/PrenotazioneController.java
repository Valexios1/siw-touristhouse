package it.uniroma3.siw.touristhouse.controller;

import it.uniroma3.siw.touristhouse.model.Camera;
import it.uniroma3.siw.touristhouse.model.Credentials;
import it.uniroma3.siw.touristhouse.model.Prenotazione;
import it.uniroma3.siw.touristhouse.model.User;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAdmin(authentication)) {
            // L'amministratore vede tutte le prenotazioni.
            model.addAttribute("prenotazioni", prenotazioneService.findAll());
        } else {
            // Un utente normale vede solo le proprie prenotazioni.
            Credentials credentials = credentialService.getCredentials(authentication.getName());
            model.addAttribute("prenotazioni", prenotazioneService.findByUtente(credentials.getUser().getId()));
        }
        return "prenotazione/lista";
    }

    @GetMapping("/prenotazioni/{id}")
    public String getPrenotazioni(@PathVariable Long id, Model model) {
        Prenotazione prenotazione = caricaPrenotazioneAccessibile(id);
        model.addAttribute("prenotazione", prenotazione);
        model.addAttribute("camera", prenotazione.getCamera());
        model.addAttribute("utente", prenotazione.getUtente());
        model.addAttribute("statiPagamento", Prenotazione.StatoPagamento.values());
        return "prenotazione/dettaglio";
    }

    @GetMapping("/prenotazioni/nuovaprenotazione")
    public String showForm(Model model) {
        model.addAttribute("prenotazione", new Prenotazione());
        model.addAttribute("camere", cameraService.findAll());
        return "prenotazione/nuovaprenotazione";
    }

    @PostMapping("/prenotazioni/nuovaprenotazione")
    public String save(@Valid @ModelAttribute("prenotazione") Prenotazione prenotazione,
                       BindingResult bindingResult,
                       @RequestParam(value = "azione", required = false, defaultValue = "conferma") String azione,
                       Model model) {
        model.addAttribute("camere", cameraService.findAll());

        if ("calcola".equals(azione)) {
            Camera selezionata = prenotazione.getCamera();
            if (selezionata == null || selezionata.getId() == null) {
                bindingResult.rejectValue("camera.id", "camera.obbligatoria", "Seleziona una camera");
            }
            if (bindingResult.hasErrors()) {
                return "prenotazione/nuovaprenotazione";
            }
            prenotazione.setCamera(cameraService.findById(selezionata.getId()));
            model.addAttribute("prezzoCalcolato", prenotazioneService.calcolaPrezzoTotale(prenotazione));
            return "prenotazione/nuovaprenotazione";
        }

        if (bindingResult.hasErrors()) {
            return "prenotazione/nuovaprenotazione";
        }

        Camera camera = cameraService.findById(prenotazione.getCamera().getId());
        prenotazione.setCamera(camera);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Credentials credentials = credentialService.getCredentials(authentication.getName());
        prenotazione.setUtente(credentials.getUser());

        prenotazione.setStato(Prenotazione.Stato.IN_ATTESA_DI_PAGAMENTO);
        prenotazione.setStatoPagamento(Prenotazione.StatoPagamento.IN_SOSPESO);

        try {
            this.prenotazioneService.savePrenotazione(prenotazione);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("errore", e.getMessage());
            return "prenotazione/nuovaprenotazione";
        }
        return "redirect:/";
    }

    @GetMapping("/prenotazioni/{id}/modifica")
    public String showModificaForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Prenotazione prenotazione = caricaPrenotazioneAccessibile(id);

        if (!modificabile(prenotazione)) {
            redirectAttributes.addFlashAttribute("errore", "La prenotazione non può più essere modificata.");
            return "redirect:/prenotazioni/" + id;
        }

        model.addAttribute("prenotazione", prenotazione);
        model.addAttribute("camera", prenotazione.getCamera());
        return "prenotazione/modifica";
    }

    @PostMapping("/prenotazioni/{id}/modifica")
    public String modifica(@PathVariable Long id,
                           @Valid @ModelAttribute("prenotazione") Prenotazione prenotazione,
                           BindingResult bindingResult,
                           @RequestParam(value = "azione", required = false, defaultValue = "conferma") String azione,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        Prenotazione esistente = caricaPrenotazioneAccessibile(id);

        if (!modificabile(esistente)) {
            redirectAttributes.addFlashAttribute("errore", "La prenotazione non può più essere modificata.");
            return "redirect:/prenotazioni/" + id;
        }

        // La camera non è modificabile: si mantiene quella della prenotazione esistente.
        prenotazione.setCamera(esistente.getCamera());
        model.addAttribute("camera", esistente.getCamera());

        if ("calcola".equals(azione)) {
            if (bindingResult.hasErrors()) {
                return "prenotazione/modifica";
            }
            model.addAttribute("prezzoCalcolato", prenotazioneService.calcolaPrezzoTotale(prenotazione));
            return "prenotazione/modifica";
        }

        if (bindingResult.hasErrors()) {
            return "prenotazione/modifica";
        }

        try {
            this.prenotazioneService.modificaPrenotazione(id, prenotazione);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("errore", e.getMessage());
            return "prenotazione/modifica";
        }
        return "redirect:/prenotazioni/" + id;
    }

    /**
     * Aggiorna lo stato del pagamento. URL sotto /admin/**: riservato all'amministratore.
     */
    @PostMapping("/admin/prenotazioni/{id}/statoPagamento")
    public String aggiornaStatoPagamento(@PathVariable Long id,
                                         @RequestParam("statoPagamento") Prenotazione.StatoPagamento statoPagamento,
                                         RedirectAttributes redirectAttributes) {
        this.prenotazioneService.aggiornaStatoPagamento(id, statoPagamento);
        redirectAttributes.addFlashAttribute("successo", "Stato del pagamento aggiornato.");
        return "redirect:/prenotazioni/" + id;
    }

    @PostMapping("/prenotazioni/{id}/elimina")
    public String elimina(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Prenotazione prenotazione = caricaPrenotazioneAccessibile(id);

        if (!modificabile(prenotazione)) {
            redirectAttributes.addFlashAttribute("errore", "La prenotazione non può più essere eliminata.");
            return "redirect:/prenotazioni/" + id;
        }

        this.prenotazioneService.eliminaPrenotazione(id);
        redirectAttributes.addFlashAttribute("successo", "Prenotazione eliminata con successo.");
        return "redirect:/prenotazioni";
    }

    /**
     * Risolve l'utente corrente (amministratore a parte) e delega al service
     * il controllo di proprietà della prenotazione.
     */
    private Prenotazione caricaPrenotazioneAccessibile(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAdmin(authentication)) {
            return prenotazioneService.findById(id);
        }
        User utente = credentialService.getCredentials(authentication.getName()).getUser();
        return prenotazioneService.findAccessibile(id, utente);
    }

    private boolean modificabile(Prenotazione prenotazione) {
        return prenotazione.getStato() == Prenotazione.Stato.IN_ATTESA_DI_PAGAMENTO;
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(Credentials.ADMIN_ROLE));
    }
}
