package it.uniroma3.siw.touristhouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.touristhouse.model.Camera;
import it.uniroma3.siw.touristhouse.service.CameraService;

@Controller
public class HomeController {

    @Autowired
    private CameraService cameraService;

    @GetMapping("/")
    public String getHome(Model model) {
        model.addAttribute("camere", cameraService.findAll());
        return "index";
    }
}
