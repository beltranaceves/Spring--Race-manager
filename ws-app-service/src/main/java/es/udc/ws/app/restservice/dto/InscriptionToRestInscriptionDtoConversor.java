package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.inscription.Inscription;

public class InscriptionToRestInscriptionDtoConversor {

    public static RestInscriptionDto toRestInscriptionDto(Inscription inscription) {
        return new RestInscriptionDto(inscription.getInscriptionId(), inscription.getRaceId(),
                inscription.getDorsalNumber(), inscription.getCollected());
    }
}
