package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.raceservice.RaceServiceFactory;
import es.udc.ws.app.model.raceservice.exceptions.AlreadyInscribedException;
import es.udc.ws.app.model.raceservice.exceptions.InscriptionDateOverException;
import es.udc.ws.app.model.raceservice.exceptions.MaxParticipantsException;
import es.udc.ws.app.model.inscription.Inscription;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.List;

public class ThriftRaceServiceImpl implements ThriftRaceService.Iface {

    @Override
    public List<ThriftInscriptionDto> findInscriptionByUserEmail(String userEmail) throws ThriftInputValidationException {

        List<Inscription> inscriptions = null;
        try {
            inscriptions = RaceServiceFactory.getService().findInscriptionByUserEmail(userEmail);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }

        return InscriptionToThriftInscriptionDtoConversor.toThriftInscriptionDtos(inscriptions);

    }

    @Override
    public long inscribeRace(long raceId, String userEmail, String creditCardNumber) throws ThriftInputValidationException,
            ThriftInstanceNotFoundException, ThriftAlreadyInscribedException, ThriftInscriptionDateOverException,
            ThriftMaxParticipantsException {

        return 0;

    }


}

