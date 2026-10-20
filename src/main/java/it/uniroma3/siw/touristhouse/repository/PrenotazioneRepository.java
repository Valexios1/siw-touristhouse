package it.uniroma3.siw.touristhouse.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.touristhouse.model.Prenotazione;

public interface PrenotazioneRepository extends CrudRepository<Prenotazione, Long> {

    /*
     * Verifica se esiste una prenotazione sulla camera indicata che si sovrappone
     * al periodo [dataCheckIn, dataCheckOut).
     *
     * Due intervalli si sovrappongono quando l'esistente inizia prima della fine del
     * nuovo e finisce dopo l'inizio del nuovo:
     *     esistente.dataCheckIn  < nuovo.dataCheckOut
     *     esistente.dataCheckOut > nuovo.dataCheckIn
     * (il confronto e' incrociato, per questo i parametri sono invertiti rispetto al nome)
     *
     * Le disuguaglianze sono strette: se il check-out di una prenotazione coincide con
     * il check-in del nuovo soggiorno NON c'e' conflitto (turnover nello stesso giorno).
     * Genera: WHERE camera.id = cameraId AND dataCheckIn < dataCheckOut AND dataCheckOut > dataCheckIn
     */
    boolean existsByCameraIdAndDataCheckInLessThanAndDataCheckOutGreaterThan(
            Long cameraId,
            LocalDate dataCheckOut,
            LocalDate dataCheckIn);

    /*
     * Come il metodo precedente, ma esclude dal controllo la prenotazione con l'id
     * indicato (condizione aggiuntiva "AND id <> :id").
     * Serve in fase di modifica di una prenotazione esistente: senza questa esclusione
     * la prenotazione troverebbe se stessa come conflitto.
     */
    boolean existsByCameraIdAndIdNotAndDataCheckInLessThanAndDataCheckOutGreaterThan(
            Long cameraId,
            Long id,
            LocalDate dataCheckOut,
            LocalDate dataCheckIn);

    /*
     * Restituisce tutte le prenotazioni effettuate dall'utente indicato,
     * usato per popolare l'area personale del cliente.
     */
    List<Prenotazione> findByUtenteId(Long utenteId);
}