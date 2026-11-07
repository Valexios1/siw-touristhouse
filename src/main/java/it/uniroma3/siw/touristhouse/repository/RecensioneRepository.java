package it.uniroma3.siw.touristhouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.touristhouse.model.Recensione;

public interface RecensioneRepository extends JpaRepository<Recensione, Long> {

    /*
     * Le 5 recensioni più recenti, usate nella pagina iniziale.
     */
    List<Recensione> findTop5ByOrderByDataCreazioneDesc();

    /*
     * Tutte le recensioni scritte dall'utente indicato (al massimo una per camera).
     */
    List<Recensione> findByUtenteId(Long utenteId);

    /*
     * Media dei voti delle recensioni di una camera. Restituisce null se non ci sono recensioni.
     */
    @Query("SELECT AVG(r.voto) FROM Recensione r WHERE r.camera.id = :cameraId")
    Double findMediaVotoByCameraId(@Param("cameraId") Long cameraId);
}
