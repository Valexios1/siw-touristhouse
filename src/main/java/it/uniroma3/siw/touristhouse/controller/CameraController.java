package it.uniroma3.siw.touristhouse.controller;

import it.uniroma3.siw.touristhouse.model.Camera;
import it.uniroma3.siw.touristhouse.model.RegolaPrezzo;
import it.uniroma3.siw.touristhouse.service.CameraService;
import it.uniroma3.siw.touristhouse.service.ImmagineService;
import it.uniroma3.siw.touristhouse.service.RegolaPrezzoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CameraController {

    @Autowired
    private CameraService cameraService;

    @Autowired
    private RegolaPrezzoService regolaPrezzoService;

    @Autowired
    private ImmagineService immagineService;

    @GetMapping("/camere")
    public String getCamere(Model model) {
        model.addAttribute("camere", cameraService.findAll());
        return "camera/lista";
    }

    @GetMapping("/camere/{id}")
    public String getCamera(@PathVariable Long id, Model model) {
        Camera camera = cameraService.findById(id);
        model.addAttribute("camera", camera);
        model.addAttribute("foto", immagineService.getImmagini("camere/" + camera.getNumeroCamera()));
        return "camera/dettaglio";
    }

    @GetMapping("/admin/nuovacamera")
    public String showForm(Model model) {
        model.addAttribute("camera", new Camera());
        model.addAttribute("tipologie", Camera.Tipologia.values());
        model.addAttribute("stati", Camera.Stato.values());
        return "admin/nuovacamera";
    }

    @PostMapping("/admin/nuovacamera")
    public String save(@Valid @ModelAttribute("camera") Camera camera,
                       BindingResult bindingResult, Model model) {
        model.addAttribute("tipologie", Camera.Tipologia.values());
        model.addAttribute("stati", Camera.Stato.values());
        if (bindingResult.hasErrors()) {
            return "admin/nuovacamera";
        }
        try {
            this.cameraService.saveCamera(camera);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("errore", e.getMessage());
            return "admin/nuovacamera";
        }
        return "redirect:/camere";
    }

    @GetMapping("/admin/camera/{id}/regole")
    public String getRegoleCamera(@PathVariable Long id, Model model) {
        Camera camera = cameraService.findById(id);
        model.addAttribute("camera", camera);
        model.addAttribute("regole", regolaPrezzoService.findAll());
        model.addAttribute("regoleSelezionateIds",
                camera.getRegolePrezzo() == null ? List.of()
                        : camera.getRegolePrezzo().stream().map(RegolaPrezzo::getId).toList());
        return "camera/regole";
    }

    @PostMapping("/admin/camera/{id}/regole")
    public String salvaRegoleCamera(@PathVariable Long id,
                                    @RequestParam(value = "regoleIds", required = false) List<Long> regoleIds) {
        cameraService.associaRegole(id, regoleIds);
        return "redirect:/camere/" + id;
    }
}
