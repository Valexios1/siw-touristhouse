package it.uniroma3.siw.touristhouse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.uniroma3.siw.touristhouse.model.Recensione;

public interface RecensioneRepository extends JpaRepository<Recensione, Long> {

    /*
     * Le 5 recensioni più recenti, usate nella pagina iniziale.
     */
    List<Recensione> findTop5ByOrderByDataCreazioneDesc();

    /*
     * La recensione dell'utente indicato (ogni utente ne ha al massimo una).
     */
    Optional<Recensione> findByUtenteId(Long utenteId);
}
