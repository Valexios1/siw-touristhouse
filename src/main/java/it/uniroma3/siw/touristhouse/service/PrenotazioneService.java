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

        if(prenotazioneRepository.)
        if (prenotazioneRepository.existsByCameraIdAndDataCheckInLessThanAndDataCheckOutGreaterThan(prenotazione.getCamera().getId(),
                prenotazione.getDataCheckIn(), prenotazione.getDataCheckOut())) {
            throw new IllegalArgumentException("la prenotazione non è valida poichè la camera " + prenotazione.getCamera().getNumeroCamera() +
                    " è occupata tra il "+ prenotazione.getDataCheckIn() +" e il " + prenotazione.getDataCheckOut());
        }
        this.prenotazioneRepository.save(prenotazione);
    }
}