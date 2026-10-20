package it.uniroma3.siw.touristhouse.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

/**
 * Recupera le foto di una camera leggendo i file presenti in
 * src/main/resources/static/images/camere/{numeroCamera}/.
 * Le foto non sono salvate nel database: basta copiare i file nella cartella
 * della camera perche' vengano mostrate nel dettaglio.
 */
@Service
public class CameraImageService {

    private static final String BASE_LOCATION = "classpath*:static/images/camere/";
    private static final List<String> ESTENSIONI = List.of(".jpg", ".jpeg", ".png", ".gif", ".webp");

    private final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    public List<String> getImmagini(Integer numeroCamera) {
        if (numeroCamera == null) {
            return List.of();
        }

        List<String> foto = new ArrayList<>();
        try {
            String pattern = BASE_LOCATION + numeroCamera + "/*";
            for (Resource resource : resolver.getResources(pattern)) {
                String filename = resource.getFilename();
                if (filename != null && isImmagine(filename)) {
                    foto.add("/images/camere/" + numeroCamera + "/" + filename);
                }
            }
        } catch (IOException e) {
            return List.of();
        }

        foto.sort(Comparator.naturalOrder());
        return foto;
    }

    private boolean isImmagine(String filename) {
        String lower = filename.toLowerCase();
        return ESTENSIONI.stream().anyMatch(lower::endsWith);
    }
}
