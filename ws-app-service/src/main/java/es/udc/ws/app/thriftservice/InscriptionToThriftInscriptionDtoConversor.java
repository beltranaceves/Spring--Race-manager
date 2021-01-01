package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.inscription.Inscription;
import es.udc.ws.app.thrift.ThriftInscriptionDto;

public class InscriptionToThriftInscriptionDtoConversor {

    public static ThriftInscriptionDto toThriftInscriptionDto(Inscription inscription) {

        return new ThriftInscriptionDto(inscription.getInscriptionId(), inscription.getRaceId(),
                inscription.getDorsalNumber(), inscription.getCollected());

    }
}
