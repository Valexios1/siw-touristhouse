package it.uniroma3.siw.touristhouse.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.touristhouse.exception.RecensioneNotFoundException;
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

    public Recensione findByUtente(Long utenteId) {
        return recensioneRepository.findByUtenteId(utenteId).orElse(null);
    }

    /**
     * Un utente può recensire solo se ha almeno una prenotazione con pagamento completato.
     */
    public boolean puoRecensire(Long utenteId) {
        return utenteId != null && prenotazioneRepository.existsByUtenteIdAndStatoPagamento(
                utenteId, Prenotazione.StatoPagamento.COMPLETATO);
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
