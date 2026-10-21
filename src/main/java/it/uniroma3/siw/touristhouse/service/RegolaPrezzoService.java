package it.uniroma3.siw.touristhouse.service;

import it.uniroma3.siw.touristhouse.exception.RegolaPrezzoNotFoundException;
import it.uniroma3.siw.touristhouse.model.Camera;
import it.uniroma3.siw.touristhouse.model.RegolaPrezzo;
import it.uniroma3.siw.touristhouse.repository.CameraRepository;
import it.uniroma3.siw.touristhouse.repository.RegolaPrezzoRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegolaPrezzoService {

    @Autowired
    private RegolaPrezzoRepository regolaPrezzoRepository;

    @Autowired
    private CameraRepository cameraRepository;

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

    @Transactional
    public void eliminaRegolaPrezzo(Long id) {
        RegolaPrezzo regola = findById(id);

        // Sgancia la regola da tutte le camere (lato proprietario della relazione),
        // altrimenti resterebbero righe orfane nella tabella di join.
        if (regola.getCamere() != null) {
            for (Camera camera : regola.getCamere()) {
                if (camera.getRegolePrezzo() != null) {
                    camera.getRegolePrezzo().removeIf(r -> r.getId().equals(regola.getId()));
                    cameraRepository.save(camera);
                }
            }
        }

        regolaPrezzoRepository.delete(regola);
    }
}
