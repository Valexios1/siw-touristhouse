package it.uniroma3.siw.touristhouse.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.touristhouse.model.Camera;
import it.uniroma3.siw.touristhouse.model.Prenotazione;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {

    /*
     * Restituisce le prenotazioni sulla camera indicata che si sovrappongono
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
    List<Prenotazione> findByCameraIdAndDataCheckInLessThanAndDataCheckOutGreaterThan(
            Long cameraId,
            LocalDate dataCheckOut,
            LocalDate dataCheckIn);

    /*
     * Come il metodo precedente, ma esclude dal controllo la prenotazione con l'id
     * indicato (condizione aggiuntiva "AND id <> :id").
     * Serve in fase di modifica di una prenotazione esistente: senza questa esclusione
     * la prenotazione troverebbe se stessa come conflitto.
     */
    List<Prenotazione> findByCameraIdAndIdNotAndDataCheckInLessThanAndDataCheckOutGreaterThan(
            Long cameraId,
            Long id,
            LocalDate dataCheckOut,
            LocalDate dataCheckIn);

    /*
     * Restituisce tutte le prenotazioni effettuate dall'utente indicato,
     * usato per popolare l'area personale del cliente.
     */
    List<Prenotazione> findByUtenteId(Long utenteId);

    /*
     * Verifica se la camera indicata ha almeno una prenotazione.
     * Usato per impedire la cancellazione di una camera ancora prenotata.
     */
    boolean existsByCameraId(Long cameraId);

    /*
     * Camere che l'utente ha prenotato con pagamento completato e che non ha ancora recensito.
     * Sono le uniche recensibili: una recensione per utente e per camera.
     */
    @Query("""
            SELECT DISTINCT p.camera
            FROM Prenotazione p
            WHERE p.utente.id = :utenteId
              AND p.statoPagamento = :stato
              AND p.camera.id NOT IN (
                  SELECT r.camera.id FROM Recensione r WHERE r.utente.id = :utenteId
              )
            """)
    List<Camera> findCamereRecensibiliByUtenteId(@Param("utenteId") Long utenteId,
                                                 @Param("stato") Prenotazione.StatoPagamento stato);
}