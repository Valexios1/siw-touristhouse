package it.uniroma3.siw.touristhouse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String getPannelloAdmin() {
        return "admin/dashboard";
    }
}
