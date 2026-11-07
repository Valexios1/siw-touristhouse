package it.uniroma3.siw.touristhouse.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.touristhouse.exception.RecensioneNotFoundException;
import it.uniroma3.siw.touristhouse.model.Camera;
import it.uniroma3.siw.touristhouse.model.Prenotazione;
import it.uniroma3.siw.touristhouse.model.Recensione;
import it.uniroma3.siw.touristhouse.repository.PrenotazioneRepository;
import it.uniroma3.siw.touristhouse.repository.RecensioneRepository;

@Service
public class RecensioneService {

    @Autowired
    private RecensioneRepository recensioneRepository;

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    public List<Recensione> ultime5() {
        return recensioneRepository.findTop5ByOrderByDataCreazioneDesc();
    }

    public List<Recensione> findAll() {
        return recensioneRepository.findAll();
    }

    public Recensione findById(Long id) {
        return recensioneRepository.findById(id).orElseThrow(() -> new RecensioneNotFoundException(id));
    }

    public List<Recensione> findByUtente(Long utenteId) {
        return recensioneRepository.findByUtenteId(utenteId);
    }

    /**
     * Camere che l'utente può ancora recensire: prenotate con pagamento completato
     * e non ancora recensite.
     */
    public List<Camera> camereRecensibili(Long utenteId) {
        if (utenteId == null) {
            return List.of();
        }
        return prenotazioneRepository.findCamereRecensibiliByUtenteId(
                utenteId, Prenotazione.StatoPagamento.COMPLETATO);
    }

    /**
     * Un utente può recensire solo una camera che ha prenotato con pagamento completato.
     */
    public boolean puoRecensire(Long utenteId) {
        return !camereRecensibili(utenteId).isEmpty();
    }

    /**
     * Media dei voti delle recensioni di una camera; null se non ci sono recensioni.
     */
    public Double mediaVotoByCameraId(Long cameraId) {
        return recensioneRepository.findMediaVotoByCameraId(cameraId);
    }

    public Recensione salva(Recensione recensione) {
        recensione.setDataCreazione(LocalDateTime.now());
        return recensioneRepository.save(recensione);
    }

    public void modifica(Long id, Recensione modifiche) {
        Recensione esistente = findById(id);
        esistente.setVoto(modifiche.getVoto());
        esistente.setTesto(modifiche.getTesto());
        recensioneRepository.save(esistente);
    }

    public void elimina(Long id) {
        Recensione recensione = findById(id);
        recensioneRepository.delete(recensione);
    }
}
