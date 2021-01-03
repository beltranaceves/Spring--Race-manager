package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientInscriptionDto;
import es.udc.ws.app.client.service.dto.ClientRaceDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientRaceService {

    public List<ClientInscriptionDto> findInscriptionByUserEmail(String userEmail) throws InputValidationException;

    public Long inscribeRace(Long raceId, String userEmail, String creditCardNumber)
            throws InstanceNotFoundException, InputValidationException, ClientAlreadyInscribedException,
            ClientInscriptionDateOverException, ClientMaxParticipantsException;

    public int collectInscription(Long inscriptionId, String creditCardNumber)
            throws InputValidationException, InstanceNotFoundException, ClientDorsalAlreadyCollectedException,
            ClientCreditCardDoesNotMatchException;

    public int findRace(Long raceId) throws InputValidationException, InstanceNotFoundException;

    public List<ClientRaceDto> findRacesByDateAndCity(String date, String city) throws InputValidationException;
    
    public Long addRace(ClientRaceDto race) throws InputValidationException;
}
