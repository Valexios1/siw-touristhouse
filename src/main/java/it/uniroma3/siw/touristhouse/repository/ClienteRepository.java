package it.uniroma3.siw.touristhouse.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.touristhouse.model.Cliente;

public interface ClienteRepository extends CrudRepository<Cliente, Long> {
}