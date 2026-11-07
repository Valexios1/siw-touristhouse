package it.uniroma3.siw.touristhouse.service;


import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.touristhouse.exception.CameraNotFoundException;
import it.uniroma3.siw.touristhouse.exception.RegolaPrezzoNotFoundException;
import it.uniroma3.siw.touristhouse.model.Camera;
import it.uniroma3.siw.touristhouse.model.RegolaPrezzo;
import it.uniroma3.siw.touristhouse.repository.CameraRepository;
import it.uniroma3.siw.touristhouse.repository.PrenotazioneRepository;
import it.uniroma3.siw.touristhouse.repository.RegolaPrezzoRepository;

@Service
public class CameraService {

    @Autowired
    private CameraRepository cameraRepository;

    @Autowired
    private RegolaPrezzoRepository regolaPrezzoRepository;

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Transactional(readOnly = true)
    public List<Camera> findAll() {
        return cameraRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Camera findById(Long id) {
        return cameraRepository.findById(id).orElseThrow(() -> new CameraNotFoundException(id));
    }

    @Transactional
    public Camera saveCamera(Camera camera) {
        if (cameraRepository.existsByNumeroCamera((camera.getNumeroCamera()))) {
            throw new IllegalArgumentException("Il numero di camera " + camera.getNumeroCamera() + " è già in uso.");
        }
        return cameraRepository.save(camera);
    }

    @Transactional
    public void eliminaCamera(Long id) {
        Camera camera = findById(id);

        // Non si cancella una camera che ha prenotazioni: si perderebbero dei dati.
        if (prenotazioneRepository.existsByCameraId(id)) {
            throw new IllegalArgumentException("Impossibile eliminare la camera " + camera.getNumeroCamera()
                    + ": ha delle prenotazioni associate.");
        }

        cameraRepository.delete(camera);
    }

    @Transactional
    public Camera associaRegole(Long cameraId, List<Long> regoleIds) {
        Camera camera = findById(cameraId);

        if (regoleIds != null && !regoleIds.isEmpty()) {
            List<RegolaPrezzo> regole = regolaPrezzoRepository.findAllById(regoleIds);

            // Verifica che tutti gli ID richiesti siano stati trovati
            if (regole.size() != regoleIds.size()) {
                throw new RegolaPrezzoNotFoundException(0L);
            }
            camera.setRegolePrezzo(regole);
        } else {
            camera.getRegolePrezzo().clear(); // Svuota la lista se regoleIds è nullo o vuoto
        }

        return cameraRepository.save(camera);
    }
}
