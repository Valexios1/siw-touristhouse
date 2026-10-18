package it.uniroma3.siw.touristhouse.controller;

import it.uniroma3.siw.touristhouse.model.Camera;
import it.uniroma3.siw.touristhouse.service.CameraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CameraController {

    @Autowired
    private CameraService cameraService;

    @GetMapping("/camere")
    public String getCamere(Model model) {
        model.addAttribute("camere", cameraService.findAll());
        return "camera/lista";
    }

    @GetMapping("/camere/{id}")
    public String getCamera(@PathVariable Long id, Model model) {
        Camera camera = cameraService.findById(id);
        model.addAttribute("camera", camera);
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
}