package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.inscription.Inscription;
import es.udc.ws.app.model.raceservice.RaceServiceFactory;
import es.udc.ws.app.model.raceservice.exceptions.AlreadyInscribedException;
import es.udc.ws.app.model.raceservice.exceptions.InscriptionDateOverException;
import es.udc.ws.app.model.raceservice.exceptions.MaxParticipantsException;
import es.udc.ws.app.restservice.dto.InscriptionToRestInscriptionDtoConversor;
import es.udc.ws.app.restservice.dto.RestInscriptionDto;
import es.udc.ws.app.restservice.json.JsonToExceptionConversor;
import es.udc.ws.app.restservice.json.JsonToRestInscriptionDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.exceptions.ParsingException;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class InscriptionsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());
        if (path == null || path.length() == 0) {
            String userEmail = req.getParameter("useremail");
            List<Inscription> inscriptions;
            try {
                inscriptions = RaceServiceFactory.getService().findInscriptionByUserEmail(userEmail);
            } catch (InputValidationException e) {
                ServletUtils
                        .writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                                JsonToExceptionConversor.toInputValidationException(new InputValidationException(
                                        "Invalid Request: " + "parameter 'userEmail' is invalid '" + userEmail + "'")),
                                null);
                return;
            }
            List<RestInscriptionDto> inscriptionDtos = InscriptionToRestInscriptionDtoConversor.toRestInscriptionDtos(inscriptions);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    JsonToRestInscriptionDtoConversor.toArrayNode(inscriptionDtos), null);
        } else {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid Request: " + "invalid path " + path)),
                    null);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());
        if (path != null && path.length() > 0) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid Request: " + "invalid path " + path)),
                    null);
            return;
        }
        String raceIdParameter = req.getParameter("raceId");
        if (raceIdParameter == null) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid Request: " + "parameter 'raceId' is mandatory")),
                    null);
            return;
        }
        Long raceId;
        try {
            raceId = Long.valueOf(raceIdParameter);
        } catch (NumberFormatException ex) {
            ServletUtils
                    .writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                            JsonToExceptionConversor.toInputValidationException(new InputValidationException(
                                    "Invalid Request: " + "parameter 'raceId' is invalid '" + raceIdParameter + "'")),
                            null);

            return;
        }
        String userEmail = req.getParameter("userEmail");
        if (userEmail == null) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid Request: " + "parameter 'userEmail' is mandatory")),
                    null);
            return;
        }
        String creditCardNumber = req.getParameter("creditCardNumber");
        if (creditCardNumber == null) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(new InputValidationException(
                            "Invalid Request: " + "parameter 'creditCardNumber' is mandatory")),
                    null);

            return;
        }

        /* Inscribe race. */
        Long inscriptionId;
        try {
            inscriptionId = RaceServiceFactory.getService().inscribeRace(raceId, userEmail, creditCardNumber);
        } catch (InstanceNotFoundException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    JsonToExceptionConversor.toInstanceNotFoundException(ex), null);
            return;
        } catch (InputValidationException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(ex), null);
            return;
        } catch (AlreadyInscribedException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                    JsonToExceptionConversor.toAlreadyInscribedException(ex), null);
            return;
        } catch (InscriptionDateOverException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                    JsonToExceptionConversor.toInscriptionDateOverException(ex), null);
            return;
        } catch (MaxParticipantsException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                    JsonToExceptionConversor.toMaxParticipantsException(ex), null);
            return;
        }

        /* Return inscription. */
        Inscription inscription;
        try {
            inscription = RaceServiceFactory.getService().findInscription(inscriptionId);
        } catch (InstanceNotFoundException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    JsonToExceptionConversor.toInstanceNotFoundException(ex), null);
            return;
        }

        RestInscriptionDto inscriptionDto = InscriptionToRestInscriptionDtoConversor.toRestInscriptionDto(inscription);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                JsonToRestInscriptionDtoConversor.toObjectNode(inscriptionDto), null);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());
        if (path == null | path.length() == 0) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid request: " + "invalid inscriptionId")), null);
            return;
        }
        String inscriptionIdAsString = path.substring(1);
        Long inscriptionId;
        try {
            inscriptionId = Long.valueOf(inscriptionIdAsString);
        } catch (NumberFormatException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid request: " + "invalid raceId '" + inscriptionIdAsString)),
                    null);
            return;
        }

        RestInscriptionDto inscriptionDto;
        try {
            //inscriptionDto = JsonToRestInscriptionDtoConversor
        } catch (ParsingException ex) {

        }
    }
}

