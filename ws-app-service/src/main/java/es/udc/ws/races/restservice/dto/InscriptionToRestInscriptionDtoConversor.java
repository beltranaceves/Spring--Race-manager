package es.udc.ws.races.restservice.dto;

import es.udc.ws.races.model.inscription.Inscription;

public class InscriptionToRestInscriptionDtoConversor {

    public static RestInscriptionDto toRestInscriptionDto(Inscription inscription) {
        return new RestInscriptionDto(inscription.getInscriptionId(), inscription.getRaceId(),
                inscription.getDorsalNumber(), inscription.getCollected());
    }
}
