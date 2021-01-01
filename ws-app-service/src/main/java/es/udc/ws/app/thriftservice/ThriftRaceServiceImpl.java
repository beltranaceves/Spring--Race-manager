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

        try {

            long inscriptionId = RaceServiceFactory.getService().inscribeRace(raceId, userEmail, creditCardNumber);
            return inscriptionId;

        } catch (AlreadyInscribedException e) {
            throw new ThriftAlreadyInscribedException(e.getRaceId(), e.getUserEmail());
        } catch (InscriptionDateOverException e) {
            throw new ThriftInscriptionDateOverException(e.getRaceId(), e.getDateOver().toString());
        } catch (MaxParticipantsException e) {
            throw new ThriftMaxParticipantsException(e.getRaceId(), e.getMaxParticipants());
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }

    }


}

