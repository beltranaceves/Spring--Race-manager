package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientInscriptionDto;
import es.udc.ws.app.thrift.ThriftInscriptionDto;

import java.util.ArrayList;
import java.util.List;

public class ClientInscriptionDtoToThriftInscriptionDtoConversor {

    public static ThriftInscriptionDto toThriftInscriptionDto(
            ClientInscriptionDto clientInscriptionDto) {

        Long inscriptionId = clientInscriptionDto.getInscriptionId();

        return new ThriftInscriptionDto(
                inscriptionId == null ? -1 : inscriptionId.longValue(),
                clientInscriptionDto.getRaceId(),
                clientInscriptionDto.getDorsalNumber(),
                clientInscriptionDto.getCollected());

    }

    public static List<ClientInscriptionDto> toClientInscriptionDto(List<ThriftInscriptionDto> inscriptions) {

        List<ClientInscriptionDto> clientInscriptionDtos = new ArrayList<>(inscriptions.size());

        for (ThriftInscriptionDto inscription : inscriptions) {
            clientInscriptionDtos.add(toClientInscriptionDto(inscription));
        }
        return clientInscriptionDtos;

    }

    private static ClientInscriptionDto toClientInscriptionDto(ThriftInscriptionDto inscription) {

        return new ClientInscriptionDto(
                inscription.getInscriptionId(),
                inscription.getRaceId(),
                inscription.getDorsalNumber(),
                inscription.isCollected());

    }
}
