package it.uniroma3.siw.touristhouse.repository;

import it.uniroma3.siw.touristhouse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
