package it.uniroma3.siw.touristhouse.repository;

import it.uniroma3.siw.touristhouse.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
