package it.uniroma3.siw.touristhouse.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

import it.uniroma3.siw.touristhouse.exception.PrenotazioneNotFoundException;
import it.uniroma3.siw.touristhouse.model.Camera;
import it.uniroma3.siw.touristhouse.model.Prenotazione;
import it.uniroma3.siw.touristhouse.model.RegolaPrezzo;
import it.uniroma3.siw.touristhouse.repository.PrenotazioneRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    public Prenotazione findById(Long id) {
        return this.prenotazioneRepository.findById(id).orElseThrow(() -> new PrenotazioneNotFoundException(id));
    }

    public Iterable<Prenotazione> findAll() {
        return this.prenotazioneRepository.findAll();
    }

    public void savePrenotazione(@Valid Prenotazione prenotazione) {

        Camera camera = prenotazione.getCamera();

        if (prenotazione.getDataCheckIn().isAfter(prenotazione.getDataCheckOut())) {
            throw new IllegalArgumentException("la prenotazione dal " + prenotazione.getDataCheckIn()
                    + " al " + prenotazione.getDataCheckOut() + "è stata inserita in modo scorretto");
        }

        long notti = ChronoUnit.DAYS.between(prenotazione.getDataCheckIn(), prenotazione.getDataCheckOut());
        if (notti < 2) {
            throw new IllegalArgumentException("la prenotazione deve essere di almeno 2 notti");
        }

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

        prenotazione.setPrezzoTotale(calcolaPrezzoTotale(prenotazione));

        this.prenotazioneRepository.save(prenotazione);
    }

    public float calcolaPrezzoTotale(Prenotazione prenotazione) {
        Camera camera = prenotazione.getCamera();
        long notti = ChronoUnit.DAYS.between(prenotazione.getDataCheckIn(), prenotazione.getDataCheckOut());

        List<RegolaPrezzo> regole = (camera.getRegolePrezzo() == null)
                ? List.of()
                : camera.getRegolePrezzo().stream()
                        .filter(regola -> regola.getMinimoNottiSoggiorno() == null
                                || regola.getMinimoNottiSoggiorno() <= notti)
                        .sorted(Comparator.comparing(RegolaPrezzo::getDataInizio)
                                .thenComparing(RegolaPrezzo::getId))
                        .toList();

        float totale = 0f;
        for (LocalDate giorno = prenotazione.getDataCheckIn();
             giorno.isBefore(prenotazione.getDataCheckOut());
             giorno = giorno.plusDays(1)) {

            float prezzoNotte = camera.getPrezzoBaseNotte();
            for (RegolaPrezzo regola : regole) {
                boolean copreLaNotte = !giorno.isBefore(regola.getDataInizio())
                        && !giorno.isAfter(regola.getDataFine());
                if (copreLaNotte) {
                    prezzoNotte = applicaVariazione(prezzoNotte, regola.getVariazionePrezzo());
                }
            }
            totale += prezzoNotte;
        }
        return Math.round(totale * 100f) / 100f;
    }

    private float applicaVariazione(float prezzo, String variazione) {
        if (variazione == null) {
            return prezzo;
        }
        try {
            float percentuale = Float.parseFloat(variazione);
            return prezzo * (1f + percentuale / 100f);
        } catch (NumberFormatException e) {
            return prezzo;
        }
    }
}
