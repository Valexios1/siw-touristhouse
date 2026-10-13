package it.uniroma3.siw.touristhouse.service;


import it.uniroma3.siw.touristhouse.model.Credentials;
import it.uniroma3.siw.touristhouse.repository.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CredentialService {


    @Autowired
    private CredentialRepository credentialRepository;

    public Credentials getCredentials(Long id){
        return credentialRepository.findById(id).get();
    }

    public Credentials getCredentials(String username){
        return credentialRepository.findByUsername(username).get();
    }

    public Credentials saveCredentials(Credentials credentials){
        return credentialRepository.save(credentials);
    }

}
