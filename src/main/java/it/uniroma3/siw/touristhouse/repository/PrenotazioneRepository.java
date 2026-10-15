package it.uniroma3.siw.touristhouse.repository;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.touristhouse.model.Prenotazione;

public interface PrenotazioneRepository extends CrudRepository<Prenotazione, Long> {

    boolean existsByCameraIdAndDataCheckInLessThanAndDataCheckOutGreaterThan(
            Long cameraId,
            LocalDate dataCheckOut,
            LocalDate dataCheckIn);

    boolean existsByCameraIdAndIdNotAndDataCheckInLessThanAndDataCheckOutGreaterThan(
            Long cameraId,
            Long id,
            LocalDate dataCheckOut,
            LocalDate dataCheckIn);
}