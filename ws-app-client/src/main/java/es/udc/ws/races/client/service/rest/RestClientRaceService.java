package es.udc.ws.races.client.service.rest;

import es.udc.ws.races.client.service.ClientRaceService;
import es.udc.ws.races.client.service.dto.ClientInscriptionDto;
import es.udc.ws.races.client.service.exceptions.ClientAlreadyInscribedException;
import es.udc.ws.races.client.service.exceptions.ClientInscriptionDateOverException;
import es.udc.ws.races.client.service.exceptions.ClientMaxParticipantsException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.List;

public class RestClientRaceService implements ClientRaceService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientRaceService.endpointAddress";
    private String endpointAddress;

    public List<ClientInscriptionDto> findInscriptionByUserEmail(String userEmail) throws InputValidationException {

        return null;

    }

    public Long inscribeRace(Long raceId, String userEmail, String creditCardNumber)
            throws InstanceNotFoundException, InputValidationException, ClientAlreadyInscribedException,
            ClientInscriptionDateOverException, ClientMaxParticipantsException {

        return null;

    }

}
