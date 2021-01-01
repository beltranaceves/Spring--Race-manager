package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.ClientRaceService;
import es.udc.ws.app.client.service.dto.ClientInscriptionDto;
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

        return null;

    }

    @Override
    public Long inscribeRace(Long raceId, String userEmail, String creditCardNumber) throws InstanceNotFoundException,
            InputValidationException, ClientAlreadyInscribedException, ClientInscriptionDateOverException,
            ClientMaxParticipantsException {

        return (long) 0;

    }

    @Override
    public int collectInscription(Long inscriptionId, String creditCardNumber) throws InputValidationException,
            InstanceNotFoundException, ClientDorsalAlreadyCollectedException, ClientCreditCardDoesNotMatchException {
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

}

