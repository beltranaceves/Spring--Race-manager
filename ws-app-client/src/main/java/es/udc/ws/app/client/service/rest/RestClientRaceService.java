package es.udc.ws.app.client.service.rest;

import es.udc.ws.app.client.service.ClientRaceService;
import es.udc.ws.app.client.service.dto.ClientInscriptionDto;
import es.udc.ws.app.client.service.dto.ClientRaceDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.app.client.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientInscriptionDtoConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientRaceDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

public class RestClientRaceService implements ClientRaceService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientRaceService.endpointAddress";
    private String endpointAddress;

    public List<ClientInscriptionDto> findInscriptionByUserEmail(String userEmail) throws InputValidationException {

        try {

            HttpResponse response = Request.Get(getEndpointAddress() + "inscriptions?useremail="
                    + URLEncoder.encode(userEmail, "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientInscriptionDtoConversor.toClientInscriptionDtos(response.getEntity()
                    .getContent());

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Long inscribeRace(Long raceId, String userEmail, String creditCardNumber)
            throws InstanceNotFoundException, InputValidationException, ClientAlreadyInscribedException,
            ClientInscriptionDateOverException, ClientMaxParticipantsException {

        try {

            HttpResponse response = Request.Post(getEndpointAddress() + "inscriptions").
                    bodyForm(
                            Form.form().
                                    add("raceId", Long.toString(raceId)).
                                    add("userEmail", userEmail).
                                    add("creditCardNumber", creditCardNumber).
                                    build()).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientInscriptionDtoConversor.toClientInscriptionDto(
                    response.getEntity().getContent()).getInscriptionId();

        } catch (InputValidationException | InstanceNotFoundException | ClientAlreadyInscribedException |
                    ClientInscriptionDateOverException | ClientMaxParticipantsException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public int collectInscription(Long inscriptionId, String creditCardNumber) throws InputValidationException,
            InstanceNotFoundException, ClientDorsalAlreadyCollectedException, ClientCreditCardDoesNotMatchException {

        try {

            HttpResponse response = Request.Post(getEndpointAddress() + "inscriptions").
                    bodyForm(
                            Form.form().
                                    add("inscriptionId", Long.toString(inscriptionId)).
                                    add("creditCardNumber", creditCardNumber).
                                    build()).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientInscriptionDtoConversor.toClientInscriptionDto(response.getEntity().getContent()).getDorsalNumber();

        } catch (InputValidationException | InstanceNotFoundException | ClientDorsalAlreadyCollectedException |
                    ClientCreditCardDoesNotMatchException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int findRace(Long raceId) throws InputValidationException, InstanceNotFoundException {

        try {
            HttpResponse response = Request.Get(getEndpointAddress() + "races?raceid="
                    + URLEncoder.encode(String.valueOf(raceId), "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            int placesLeft;
            placesLeft = JsonToClientRaceDtoConversor.toClientRaceDto(response.getEntity().getContent()).getMaxParticipants()
                    - JsonToClientRaceDtoConversor.toClientRaceDto(response.getEntity().getContent()).getNumberOfInscribed();

            return placesLeft;

        } catch (InputValidationException | InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private void validateStatusCode(int successCode, HttpResponse response) throws Exception {

        try {

            int statusCode = response.getStatusLine().getStatusCode();

            /* Success? */
            if (statusCode == successCode) {
                return;
            }

            /* Handler error. */
            switch (statusCode) {

                case HttpStatus.SC_NOT_FOUND:
                    throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                            response.getEntity().getContent());

                case HttpStatus.SC_BAD_REQUEST:
                    throw JsonToClientExceptionConversor.fromBadRequestErrorCode(
                            response.getEntity().getContent());

                case HttpStatus.SC_FORBIDDEN:
                    throw JsonToClientExceptionConversor.fromForbiddenErrorCode(
                            response.getEntity().getContent());

                default:
                    throw new RuntimeException("HTTP error; status code = "
                            + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public Long addRace(ClientRaceDto race) throws InputValidationException {
        try {

            HttpResponse response = Request.Post(getEndpointAddress() + "races").
                    bodyStream(toInputStream(race), ContentType.create("application/json")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientRaceDtoConversor.toClientRaceDto(response.getEntity().getContent()).getRaceId();

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private InputStream toInputStream(ClientRaceDto race) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientRaceDtoConversor.toObjectNode(race));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}