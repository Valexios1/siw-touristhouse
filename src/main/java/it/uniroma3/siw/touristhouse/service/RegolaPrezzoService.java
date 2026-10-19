package it.uniroma3.siw.touristhouse.service;

import it.uniroma3.siw.touristhouse.exception.RegolaPrezzoNotFoundException;
import it.uniroma3.siw.touristhouse.model.RegolaPrezzo;
import it.uniroma3.siw.touristhouse.repository.RegolaPrezzoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegolaPrezzoService {

    @Autowired
    private RegolaPrezzoRepository regolaPrezzoRepository;

    public Iterable<RegolaPrezzo> findAll() {
        return regolaPrezzoRepository.findAll();
    }

    public RegolaPrezzo findById(Long id) {
        return regolaPrezzoRepository.findById(id).orElseThrow(() -> new RegolaPrezzoNotFoundException(id));
    }

    public RegolaPrezzo save(RegolaPrezzo regolaPrezzo) {
        return regolaPrezzoRepository.save(regolaPrezzo);
    }

    public RegolaPrezzo modificaRegolaPrezzo(Long id, RegolaPrezzo modifiche) {
        RegolaPrezzo esistente = findById(id);
        esistente.setNome(modifiche.getNome());
        esistente.setDataInizio(modifiche.getDataInizio());
        esistente.setDataFine(modifiche.getDataFine());
        esistente.setVariazionePrezzo(modifiche.getVariazionePrezzo());
        esistente.setMinimoNottiSoggiorno(modifiche.getMinimoNottiSoggiorno());
        return save(esistente);
    }
}
