package it.uniroma3.siw.touristhouse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.touristhouse.DTO.CameraDTO;
import it.uniroma3.siw.touristhouse.service.CameraService;

@RestController
@RequestMapping("/api/camere")
public class CameraRestController {

    @Autowired
    private CameraService cameraService;

    @GetMapping
    public List<CameraDTO> getCamere() {
        return cameraService.findAll().stream().map(CameraDTO::from).toList();
    }
}
