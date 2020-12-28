package es.udc.ws.app.client.service.rest;

import es.udc.ws.app.client.service.ClientRaceService;
import es.udc.ws.app.client.service.dto.ClientInscriptionDto;
import es.udc.ws.app.client.service.exceptions.ClientAlreadyInscribedException;
import es.udc.ws.app.client.service.exceptions.ClientInscriptionDateOverException;
import es.udc.ws.app.client.service.exceptions.ClientMaxParticipantsException;
import es.udc.ws.app.client.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientInscriptionDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.net.URLEncoder;
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

                default:
                    throw new RuntimeException("HTTP error; status code = "
                            + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
