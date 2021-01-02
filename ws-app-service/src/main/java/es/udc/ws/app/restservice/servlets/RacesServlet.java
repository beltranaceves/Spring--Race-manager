package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.race.Race;
import es.udc.ws.app.model.raceservice.RaceServiceFactory;
import es.udc.ws.app.restservice.dto.RaceToRestRaceDtoConversor;
import es.udc.ws.app.restservice.json.JsonToExceptionConversor;
import es.udc.ws.app.restservice.json.JsonToRestInscriptionDtoConversor;
import es.udc.ws.app.restservice.json.JsonToRestRaceDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;
import es.udc.ws.app.restservice.dto.RestRaceDto;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("serial")
public class RacesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());
        if (path == null || path.length() == 0) {
            String date, city;

            if((date = req.getParameter("date")) != null) {
                if ((city = req.getParameter("city")) != null) {
                    //FIND BY DATE AND CITY
                } else {
                    //FIND ONLY BY DATE (NULl CITY)
                }
            } else {
                //FIND BY RACEID
                String raceIdAsString = path.substring(1);
                Long raceId = Long.valueOf(req.getParameter("raceid"));
                try {
                    raceId = Long.valueOf(raceIdAsString);
                } catch (NumberFormatException ex) {
                    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                            JsonToExceptionConversor.toInputValidationException(
                                    new InputValidationException("Invalid request: " + "invalid raceId '" + raceIdAsString)),
                            null);
                    return;
                }

                Race race;
                try{
                    race = RaceServiceFactory.getService().findRace(raceId);
                    RestRaceDto raceDto = RaceToRestRaceDtoConversor.toRestRaceDto(race);
                    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                            JsonToRestRaceDtoConversor.toObjectNode(raceDto), null);
                } catch (InstanceNotFoundException ex) {
                    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                            JsonToExceptionConversor.toInstanceNotFoundException(ex), null);
                    return;
                }

            }
        } else {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid request: " + "invalid raceId")), null);
            return;
        }

    }

}