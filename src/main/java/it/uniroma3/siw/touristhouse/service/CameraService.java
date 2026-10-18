package it.uniroma3.siw.touristhouse.service;

import it.uniroma3.siw.touristhouse.model.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.touristhouse.exception.CameraNotFoundException;
import it.uniroma3.siw.touristhouse.model.Camera;
import it.uniroma3.siw.touristhouse.repository.CameraRepository;

@Service
public class CameraService {

    @Autowired
    private CameraRepository cameraRepository;

    public Iterable<Camera> findAll() {
        return cameraRepository.findAll();
    }

    public Camera findById(Long id) {
        return cameraRepository.findById(id).orElseThrow(() -> new CameraNotFoundException(id));
    }

    public Camera saveCamera(Camera camera) {
        if (cameraRepository.existsByNumeroCamera((camera.getNumeroCamera()))) {
            throw new IllegalArgumentException("Il numero di camera " + camera.getNumeroCamera() + " è già in uso.");
        }
        return cameraRepository.save(camera);
    }
}