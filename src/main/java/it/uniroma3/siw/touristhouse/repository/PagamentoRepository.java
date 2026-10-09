package it.uniroma3.siw.touristhouse.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.touristhouse.model.Pagamento;

public interface PagamentoRepository extends CrudRepository<Pagamento, Long> {
}