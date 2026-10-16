package it.uniroma3.siw.touristhouse.service;

import it.uniroma3.siw.touristhouse.model.Camera;
import it.uniroma3.siw.touristhouse.model.Prenotazione;
import it.uniroma3.siw.touristhouse.repository.PrenotazioneRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrenotazioneService {


    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    public Prenotazione findById(Long id) {
        return this.prenotazioneRepository.findById(id).orElse(null);
    }

    public void savePrenotazione(@Valid Prenotazione prenotazione) {

        Camera camera = prenotazione.getCamera();

        if (camera.getStatoAttuale() == Camera.Stato.IN_MANUTENZIONE) {
            throw new IllegalArgumentException("la camera " + camera.getNumeroCamera() +
                    " è in manutenzione e non può essere prenotata");
        }

        int numeroOspiti = prenotazione.getNumeroAdulti() + prenotazione.getNumeroBambini();
        if (numeroOspiti > camera.getCapienzaMassima()) {
            throw new IllegalArgumentException("la camera " + camera.getNumeroCamera() +
                    " ha capienza massima di " + camera.getCapienzaMassima() +
                    " ospiti, richiesti " + numeroOspiti);
        }

        if (prenotazioneRepository.existsByCameraIdAndDataCheckInLessThanAndDataCheckOutGreaterThan(
                camera.getId(), prenotazione.getDataCheckOut(), prenotazione.getDataCheckIn())) {
            throw new IllegalArgumentException("la prenotazione non è valida poichè la camera " + camera.getNumeroCamera() +
                    " è occupata tra il " + prenotazione.getDataCheckIn() + " e il " + prenotazione.getDataCheckOut());
        }

        this.prenotazioneRepository.save(prenotazione);
    }
}