package it.uniroma3.siw.touristhouse.controller;

import it.uniroma3.siw.touristhouse.model.RegolaPrezzo;
import it.uniroma3.siw.touristhouse.service.RegolaPrezzoService;
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
public class RegolaPrezzoController {

    @Autowired
    private RegolaPrezzoService regolaPrezzoService;

    @GetMapping("/admin/regolePrezzo")
    public String getRegolePrezzo(Model model) {
        model.addAttribute("regolePrezzo", regolaPrezzoService.findAll());
        return "regolaPrezzo/lista";
    }

    @GetMapping("/admin/regolaPrezzo/{id}")
    public String getRegolaPrezzo(@PathVariable Long id, Model model) {
        RegolaPrezzo regolaPrezzo = regolaPrezzoService.findById(id);
        model.addAttribute("regolaPrezzo", regolaPrezzo);
        model.addAttribute("camere", regolaPrezzo.getCamere());
        return "regolaPrezzo/dettaglio";
    }

    @GetMapping("/admin/nuovaRegolaPrezzo")
    public String nuovaRegolaPrezzo(Model model) {
        model.addAttribute("regolaPrezzo", new RegolaPrezzo());
        return "regolaPrezzo/nuova";
    }

    @PostMapping("/admin/nuovaRegolaPrezzo")
    public String save(@Valid @ModelAttribute("regolaPrezzo") RegolaPrezzo regolaPrezzo,
                       BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "regolaPrezzo/nuova";
        }
        RegolaPrezzo salvata = this.regolaPrezzoService.save(regolaPrezzo);
        return "redirect:/admin/regolaPrezzo/" + salvata.getId();
    }

    @GetMapping("/admin/regolaPrezzo/{id}/modifica")
    public String modificaRegolaPrezzo(@PathVariable Long id, Model model) {
        model.addAttribute("regolaPrezzo", regolaPrezzoService.findById(id));
        return "regolaPrezzo/modifica";
    }

    @PostMapping("/admin/regolaPrezzo/{id}/modifica")
    public String save(@PathVariable Long id,
                       @Valid @ModelAttribute("regolaPrezzo") RegolaPrezzo regolaPrezzo,
                       BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "regolaPrezzo/modifica";
        }
        try {
            this.regolaPrezzoService.modificaRegolaPrezzo(id, regolaPrezzo);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("errore", e.getMessage());
            return "regolaPrezzo/modifica";
        }
        return "redirect:/admin/regolaPrezzo/" + id;
    }
}
