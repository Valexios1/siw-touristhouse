package it.uniroma3.siw.touristhouse.service;


import it.uniroma3.siw.touristhouse.model.Credentials;
import it.uniroma3.siw.touristhouse.repository.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CredentialService {


    @Autowired
    private CredentialRepository credentialRepository;

    @Transactional(readOnly = true)
    public Credentials getCredentials(Long id){
        return credentialRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public Credentials getCredentials(String username){
        return credentialRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    public Credentials saveCredentials(Credentials credentials){
        return credentialRepository.save(credentials);
    }

}
