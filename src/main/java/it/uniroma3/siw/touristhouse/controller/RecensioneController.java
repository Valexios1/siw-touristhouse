package it.uniroma3.siw.touristhouse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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

import it.uniroma3.siw.touristhouse.exception.RecensioneNotFoundException;
import it.uniroma3.siw.touristhouse.model.Camera;
import it.uniroma3.siw.touristhouse.model.Credentials;
import it.uniroma3.siw.touristhouse.model.Recensione;
import it.uniroma3.siw.touristhouse.model.User;
import it.uniroma3.siw.touristhouse.service.CredentialService;
import it.uniroma3.siw.touristhouse.service.RecensioneService;
import jakarta.validation.Valid;

@Controller
public class RecensioneController {

    @Autowired
    private RecensioneService recensioneService;

    @Autowired
    private CredentialService credentialService;

    @GetMapping("/recensioni/nuova")
    public String nuovaForm(Model model, RedirectAttributes redirectAttributes) {
        User utente = utenteCorrente();
        List<Camera> camere = recensioneService.camereRecensibili(utente != null ? utente.getId() : null);

        if (camere.isEmpty()) {
            redirectAttributes.addFlashAttribute("errore",
                    "Puoi recensire solo una camera che hai prenotato e pagato, e solo una volta per camera.");
            return "redirect:/";
        }

        model.addAttribute("recensione", new Recensione());
        model.addAttribute("camere", camere);
        return "recensione/form";
    }

    @PostMapping("/recensioni/nuova")
    public String nuova(@Valid @ModelAttribute("recensione") Recensione recensione,
                        BindingResult bindingResult,
                        @RequestParam(value = "cameraId", required = false) Long cameraId,
                        Model model, RedirectAttributes redirectAttributes) {
        User utente = utenteCorrente();
        List<Camera> camere = recensioneService.camereRecensibili(utente != null ? utente.getId() : null);

        if (camere.isEmpty()) {
            redirectAttributes.addFlashAttribute("errore",
                    "Puoi recensire solo una camera che hai prenotato e pagato, e solo una volta per camera.");
            return "redirect:/";
        }

        Camera camera = camere.stream()
                .filter(c -> c.getId().equals(cameraId))
                .findFirst()
                .orElse(null);
        if (camera == null) {
            bindingResult.reject("errore", "Seleziona una camera valida.");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("camere", camere);
            return "recensione/form";
        }

        recensione.setUtente(utente);
        recensione.setCamera(camera);
        this.recensioneService.salva(recensione);
        redirectAttributes.addFlashAttribute("successo", "Recensione salvata.");
        return "redirect:/";
    }

    @GetMapping("/recensioni/{id}/modifica")
    public String modificaForm(@PathVariable Long id, Model model) {
        model.addAttribute("recensione", caricaMiaRecensione(id));
        return "recensione/form";
    }

    @PostMapping("/recensioni/{id}/modifica")
    public String modifica(@PathVariable Long id,
                           @Valid @ModelAttribute("recensione") Recensione recensione,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        Recensione esistente = caricaMiaRecensione(id);

        if (bindingResult.hasErrors()) {
            // Mantiene l'id e la camera per far restare il form in modalità "modifica".
            recensione.setId(id);
            recensione.setCamera(esistente.getCamera());
            return "recensione/form";
        }

        this.recensioneService.modifica(id, recensione);
        redirectAttributes.addFlashAttribute("successo", "Recensione aggiornata.");
        return "redirect:/";
    }

    @PostMapping("/recensioni/{id}/elimina")
    public String elimina(@PathVariable Long id,
                          @RequestParam(value = "admin", required = false, defaultValue = "false") boolean daAdmin,
                          RedirectAttributes redirectAttributes) {
        Recensione recensione = recensioneService.findById(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User utente = utenteCorrente();
        boolean autore = utente != null && recensione.getUtente() != null
                && recensione.getUtente().getId().equals(utente.getId());

        if (!autore && !isAdmin(authentication)) {
            throw new RecensioneNotFoundException(id);
        }

        this.recensioneService.elimina(id);
        redirectAttributes.addFlashAttribute("successo", "Recensione eliminata.");
        return daAdmin ? "redirect:/admin/recensioni" : "redirect:/";
    }

    /**
     * Elenco di tutte le recensioni (gestione amministratore). URL sotto /admin/**.
     */
    @GetMapping("/admin/recensioni")
    public String getRecensioniAdmin(Model model) {
        model.addAttribute("recensioni", recensioneService.findAll());
        return "admin/recensioni";
    }

    /**
     * Carica la recensione verificando che sia dell'utente corrente.
     */
    private Recensione caricaMiaRecensione(Long id) {
        Recensione recensione = recensioneService.findById(id);
        User utente = utenteCorrente();

        if (utente == null || recensione.getUtente() == null
                || !recensione.getUtente().getId().equals(utente.getId())) {
            throw new RecensioneNotFoundException(id);
        }
        return recensione;
    }

    private User utenteCorrente() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        Credentials credentials = credentialService.getCredentials(authentication.getName());
        return credentials != null ? credentials.getUser() : null;
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(Credentials.ADMIN_ROLE));
    }
}
