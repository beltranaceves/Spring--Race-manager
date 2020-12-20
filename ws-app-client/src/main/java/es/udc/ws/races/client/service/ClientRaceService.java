package es.udc.ws.races.client.service;

import es.udc.ws.races.client.service.dto.ClientInscriptionDto;
import es.udc.ws.races.client.service.exceptions.ClientAlreadyInscribedException;
import es.udc.ws.races.client.service.exceptions.ClientMaxParticipantsException;
import es.udc.ws.races.client.service.exceptions.ClientInscriptionDateOverException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.util.List;

public interface ClientRaceService {

    public List<ClientInscriptionDto> findInscriptionByUserEmail(String userEmail) throws InputValidationException;

    public Long inscribeRace(Long raceId, String userEmail, String creditCardNumber)
            throws InstanceNotFoundException, InputValidationException, ClientAlreadyInscribedException,
            ClientInscriptionDateOverException, ClientMaxParticipantsException;

}
