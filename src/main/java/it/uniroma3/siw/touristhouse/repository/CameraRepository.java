package it.uniroma3.siw.touristhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.uniroma3.siw.touristhouse.model.Camera;

public interface CameraRepository extends JpaRepository<Camera, Long> {


    boolean existsByNumeroCamera(int attr0);
}