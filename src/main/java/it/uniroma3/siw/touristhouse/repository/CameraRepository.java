package it.uniroma3.siw.touristhouse.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.touristhouse.model.Camera;

public interface CameraRepository extends CrudRepository<Camera, Long> {
}