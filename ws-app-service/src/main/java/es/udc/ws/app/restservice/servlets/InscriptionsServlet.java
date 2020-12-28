package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.inscription.Inscription;
import es.udc.ws.app.model.raceservice.RaceServiceFactory;
import es.udc.ws.app.restservice.dto.InscriptionToRestInscriptionDtoConversor;
import es.udc.ws.app.restservice.dto.RestInscriptionDto;
import es.udc.ws.app.restservice.json.JsonToExceptionConversor;
import es.udc.ws.app.restservice.json.JsonToRestInscriptionDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
public class InscriptionsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

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
}

