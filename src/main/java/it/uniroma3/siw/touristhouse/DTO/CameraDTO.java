package it.uniroma3.siw.touristhouse.DTO;

import it.uniroma3.siw.touristhouse.model.Camera;

public record CameraDTO(
        Long id,
        Integer numeroCamera,
        String tipologia,
        Integer capienzaMassima,
        Integer superficieMq,
        Float prezzoBaseNotte,
        String statoAttuale) {

    public static CameraDTO from(Camera camera) {
        return new CameraDTO(
                camera.getId(),
                camera.getNumeroCamera(),
                camera.getTipologia().name(),
                camera.getCapienzaMassima(),
                camera.getSuperficieMq(),
                camera.getPrezzoBaseNotte(),
                camera.getStatoAttuale().name());
    }
}
