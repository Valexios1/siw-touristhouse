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
 * Recupera le immagini salvate sotto src/main/resources/static/images/.
 * Le immagini non sono nel database: basta copiare i file nella cartella
 * della sezione desiderata (es. "camere/101" oppure "home").
 */
@Service
public class ImmagineService {

    private static final String BASE_LOCATION = "classpath*:static/images/";
    private static final List<String> ESTENSIONI = List.of(".jpg", ".jpeg", ".png", ".gif", ".webp");

    private final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    /**
     * @param cartella percorso relativo sotto static/images (es. "camere/101" o "home")
     * @return gli URL web delle immagini trovate, in ordine alfabetico
     */
    public List<String> getImmagini(String cartella) {
        List<String> foto = new ArrayList<>();
        try {
            for (Resource resource : resolver.getResources(BASE_LOCATION + cartella + "/*")) {
                String filename = resource.getFilename();
                if (filename != null && isImmagine(filename)) {
                    foto.add("/images/" + cartella + "/" + filename);
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
