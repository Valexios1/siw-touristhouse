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

    public List<Prenotazione> findByUtente(Long utenteId) {
        return this.prenotazioneRepository.findByUtenteId(utenteId);
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

    /**
     * Calcola il prezzo totale del soggiorno sommando il prezzo di ogni singola notte.
     * Per ogni notte si parte dal prezzo base della camera e si applicano, in ordine,
     * tutte le regole di prezzo attive in quella data.
     */
    public float calcolaPrezzoTotale(Prenotazione prenotazione) {
        Camera camera = prenotazione.getCamera();
        // Numero di notti = giorni tra check-in (incluso) e check-out (escluso)
        long notti = ChronoUnit.DAYS.between(prenotazione.getDataCheckIn(), prenotazione.getDataCheckOut());

        // Considera solo le regole valide per questa durata (minimo notti rispettato),
        // ordinate per data di inizio (e per id in caso di parità) così da applicarle in modo deterministico
        List<RegolaPrezzo> regole = (camera.getRegolePrezzo() == null)
                ? List.of()
                : camera.getRegolePrezzo().stream()
                        .filter(regola -> regola.getMinimoNottiSoggiorno() <= notti)
                        .sorted(Comparator.comparing(RegolaPrezzo::getDataInizio)
                                .thenComparing(RegolaPrezzo::getId))
                        .toList();

        float totale = 0f;
        // Itera su ogni notte del soggiorno (il check-out non è una notte)
        for (LocalDate giorno = prenotazione.getDataCheckIn();
             giorno.isBefore(prenotazione.getDataCheckOut());
             giorno = giorno.plusDays(1)) {

            float prezzoNotte = camera.getPrezzoBaseNotte();
            for (RegolaPrezzo regola : regole) {
                // La regola si applica se la data corrente rientra nel suo intervallo [dataInizio, dataFine]
                boolean copreLaNotte = !giorno.isBefore(regola.getDataInizio())
                        && !giorno.isAfter(regola.getDataFine());
                if (copreLaNotte) {
                    prezzoNotte = applicaVariazione(prezzoNotte, regola.getVariazionePrezzo());
                }
            }
            totale += prezzoNotte;
        }
        // Arrotonda a 2 cifre decimali (centesimi di euro)
        return Math.round(totale * 100f) / 100f;
    }

    /**
     * Applica una variazione percentuale al prezzo.
     * Es. "+10" aumenta del 10%, "-20" riduce del 20%; una variazione nulla lascia il prezzo invariato.
     */
    private float applicaVariazione(float prezzo, String variazione) {
        if (variazione == null) {
            return prezzo;
        }
        float percentuale = Float.parseFloat(variazione);
        return prezzo * (1f + percentuale / 100f);
    }
}
