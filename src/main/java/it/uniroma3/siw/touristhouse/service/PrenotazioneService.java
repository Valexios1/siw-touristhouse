package it.uniroma3.siw.touristhouse.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

import it.uniroma3.siw.touristhouse.exception.PrenotazioneNotFoundException;
import it.uniroma3.siw.touristhouse.model.Camera;
import it.uniroma3.siw.touristhouse.model.Prenotazione;
import it.uniroma3.siw.touristhouse.model.RegolaPrezzo;
import it.uniroma3.siw.touristhouse.model.User;
import it.uniroma3.siw.touristhouse.repository.PrenotazioneRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrenotazioneService {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    public Prenotazione findById(Long id) {
        return this.prenotazioneRepository.findById(id).orElseThrow(() -> new PrenotazioneNotFoundException(id));
    }

    /**
     * Restituisce la prenotazione solo se appartiene all'utente indicato,
     * altrimenti si comporta come se non esistesse.
     */
    public Prenotazione findAccessibile(Long id, User utente) {
        Prenotazione prenotazione = findById(id);

        if (utente == null
                || prenotazione.getUtente() == null
                || !prenotazione.getUtente().getId().equals(utente.getId())) {
            throw new PrenotazioneNotFoundException(id);
        }
        return prenotazione;
    }

    public Iterable<Prenotazione> findAll() {
        return this.prenotazioneRepository.findAll();
    }

    public List<Prenotazione> findByUtente(Long utenteId) {
        return this.prenotazioneRepository.findByUtenteId(utenteId);
    }

    public void savePrenotazione(@Valid Prenotazione prenotazione) {
        valida(prenotazione, null);
        prenotazione.setPrezzoTotale(calcolaPrezzoTotale(prenotazione));
        this.prenotazioneRepository.save(prenotazione);
    }

    public void modificaPrenotazione(Long id, Prenotazione modifiche) {
        Prenotazione esistente = findById(id);

        esistente.setDataCheckIn(modifiche.getDataCheckIn());
        esistente.setDataCheckOut(modifiche.getDataCheckOut());
        esistente.setNumeroAdulti(modifiche.getNumeroAdulti());
        esistente.setNumeroBambini(modifiche.getNumeroBambini());
        esistente.setNoteCliente(modifiche.getNoteCliente());

        // Esclude la prenotazione stessa dal controllo di sovrapposizione.
        valida(esistente, id);
        esistente.setPrezzoTotale(calcolaPrezzoTotale(esistente));
        this.prenotazioneRepository.save(esistente);
    }

    /**
     * Aggiorna lo stato del pagamento (azione riservata all'amministratore) e tiene
     * allineato lo stato della prenotazione: pagamento completato -> CONFERMATA,
     * altrimenti -> IN_ATTESA_DI_PAGAMENTO.
     */
    public void aggiornaStatoPagamento(Long id, Prenotazione.StatoPagamento statoPagamento) {
        Prenotazione prenotazione = findById(id);
        prenotazione.setStatoPagamento(statoPagamento);

        if (statoPagamento == Prenotazione.StatoPagamento.COMPLETATO) {
            prenotazione.setStato(Prenotazione.Stato.CONFERMATA);
        } else {
            prenotazione.setStato(Prenotazione.Stato.IN_ATTESA_DI_PAGAMENTO);
        }

        this.prenotazioneRepository.save(prenotazione);
    }

    public void eliminaPrenotazione(Long id) {
        Prenotazione prenotazione = findById(id);
        this.prenotazioneRepository.delete(prenotazione);
    }

    /**
     * Valida la prenotazione (date, durata, stato camera, capienza, sovrapposizioni).
     * Se {@code idDaEscludere} non e' nullo, quella prenotazione viene ignorata nel
     * controllo di sovrapposizione (serve in fase di modifica).
     */
    private void valida(Prenotazione prenotazione, Long idDaEscludere) {
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

        List<Prenotazione> conflitti = (idDaEscludere == null)
                ? prenotazioneRepository.findByCameraIdAndDataCheckInLessThanAndDataCheckOutGreaterThan(
                        camera.getId(), prenotazione.getDataCheckOut(), prenotazione.getDataCheckIn())
                : prenotazioneRepository.findByCameraIdAndIdNotAndDataCheckInLessThanAndDataCheckOutGreaterThan(
                        camera.getId(), idDaEscludere, prenotazione.getDataCheckOut(), prenotazione.getDataCheckIn());

        if (!conflitti.isEmpty()) {
            // Intervallo effettivamente occupato: dal check-in più antico al check-out
            // più recente tra tutte le prenotazioni che si sovrappongono alla richiesta.
            LocalDate inizioOccupato = conflitti.stream()
                    .map(Prenotazione::getDataCheckIn)
                    .min(Comparator.naturalOrder())
                    .orElseThrow();
            LocalDate fineOccupato = conflitti.stream()
                    .map(Prenotazione::getDataCheckOut)
                    .max(Comparator.naturalOrder())
                    .orElseThrow();
            throw new IllegalArgumentException("la prenotazione non è valida poichè la camera " + camera.getNumeroCamera() +
                    " è occupata dal " + FORMATO_DATA.format(inizioOccupato)
                    + " al " + FORMATO_DATA.format(fineOccupato));
        }
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
