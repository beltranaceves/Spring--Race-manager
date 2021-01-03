package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.ClientRaceService;
import es.udc.ws.app.client.service.dto.ClientInscriptionDto;
import es.udc.ws.app.client.service.dto.ClientRaceDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.app.thrift.ThriftInputValidationException;
import es.udc.ws.app.thrift.ThriftInstanceNotFoundException;
import es.udc.ws.app.thrift.ThriftAlreadyInscribedException;
import es.udc.ws.app.thrift.ThriftInscriptionDateOverException;
import es.udc.ws.app.thrift.ThriftMaxParticipantsException;
import es.udc.ws.app.thrift.ThriftRaceService;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ThriftClientRaceService implements ClientRaceService {

    private final static String ENDPOINT_ADDRESS_PARAMETER =
            "ThriftClientRaceService.endpointAddress";

    private final static String endpointAddress =
            ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);


    @Override
    public List<ClientInscriptionDto> findInscriptionByUserEmail(String userEmail) throws InputValidationException {
        
        ThriftRaceService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try  {

            transport.open();

            return ClientInscriptionDtoToThriftInscriptionDtoConversor.toClientInscriptionDto(client.findInscriptionByUserEmail(userEmail));

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }

    }

    @Override
    public Long inscribeRace(Long raceId, String userEmail, String creditCardNumber) throws InstanceNotFoundException,
            InputValidationException, ClientAlreadyInscribedException, ClientInscriptionDateOverException,
            ClientMaxParticipantsException {

        ThriftRaceService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try  {

            transport.open();

            return client.inscribeRace(raceId, userEmail, creditCardNumber);

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (ThriftAlreadyInscribedException e) {
            throw new ClientAlreadyInscribedException(e.getRaceId(), e.getUserEmail());
        } catch (ThriftInscriptionDateOverException e) {
            throw new ClientInscriptionDateOverException(e.getRaceId(), LocalDateTime.parse(e.getDateOver(),
                    DateTimeFormatter.ISO_DATE_TIME));
        } catch (ThriftMaxParticipantsException e) {
            throw new ClientMaxParticipantsException(e.getRaceId(), e.getMaxParticipants());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }

    }

    @Override
    public int collectInscription(Long inscriptionId, String creditCardNumber) throws InputValidationException,
            InstanceNotFoundException, ClientDorsalAlreadyCollectedException, ClientCreditCardDoesNotMatchException {
        return 0;
    }

    @Override
    public int findRace(Long raceId) throws InputValidationException, InstanceNotFoundException {
        return 0;
    }

    private ThriftRaceService.Client getClient() {

        try {

            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);

            return new ThriftRaceService.Client(protocol);

        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }

    }
    
    @Override
    public Long addRace(ClientRaceDto race) throws InputValidationException {
    	return null;
    }

    @Override
    public List<ClientRaceDto> findRacesByDateAndCity(String date, String city) throws InputValidationException {
        return null;
    }
}

