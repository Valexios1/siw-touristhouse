package it.uniroma3.siw.touristhouse.service;


import it.uniroma3.siw.touristhouse.exception.UserNotFoundException;
import it.uniroma3.siw.touristhouse.model.User;
import it.uniroma3.siw.touristhouse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }


}
