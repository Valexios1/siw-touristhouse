package it.uniroma3.siw.touristhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.uniroma3.siw.touristhouse.model.RegolaPrezzo;

public interface RegolaPrezzoRepository extends JpaRepository<RegolaPrezzo, Long> {
}