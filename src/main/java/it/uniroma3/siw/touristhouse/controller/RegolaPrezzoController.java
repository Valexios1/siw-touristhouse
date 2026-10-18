package it.uniroma3.siw.touristhouse.controller;

import it.uniroma3.siw.touristhouse.model.RegolaPrezzo;
import it.uniroma3.siw.touristhouse.service.RegolaPrezzoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
}
