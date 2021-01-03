package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.race.Race;
import es.udc.ws.app.model.raceservice.RaceServiceFactory;
import es.udc.ws.app.restservice.dto.RaceToRestRaceDtoConversor;
import es.udc.ws.app.restservice.json.JsonToExceptionConversor;
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
import java.time.LocalDateTime;

@SuppressWarnings("serial")
public class RacesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());
        if (path == null || path.length() == 0) {
            String raceId, date, city;

            if((date = req.getParameter("date")) != null) {
                if ((city = req.getParameter("city")) != null) {
                    //FIND BY DATE AND CITY
                } else {
                    //FIND ONLY BY DATE (NULl CITY)
                }
            } else {
                //FIND BY RACEID
                if ((raceId = req.getParameter("raceId")) != null) {
                    Race race;
                    try{
                        race = RaceServiceFactory.getService().findRace(Long.parseLong(raceId));
                    } catch (InstanceNotFoundException ex) {
                        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                                JsonToExceptionConversor.toInstanceNotFoundException(ex), null);
                        return;
                    }
                    RestRaceDto raceDto = RaceToRestRaceDtoConversor.toRestRaceDto(race);
                    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                            JsonToRestRaceDtoConversor.toObjectNode(raceDto), null);
                } else {
                    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                            JsonToExceptionConversor.toInputValidationException(
                                    new InputValidationException("Invalid request: " + "invalid path")), null);
                    return;
                }
            }
        } else {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid request: " + "invalid path")), null);
            return;
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
        String city= req.getParameter("city");
        if (city == null) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid Request: " + "parameter 'city' is mandatory")),
                    null);
            return;
        }
        String description = req.getParameter("description");
        if (description == null) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(new InputValidationException(
                            "Invalid Request: " + "parameter 'description' is mandatory")),
                    null);

            return;
        }
        
        String dateParameter = req.getParameter("date");
        if (dateParameter == null) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid Request: " + "parameter 'date' is mandatory")),
                    null);
            return;
        }
        LocalDateTime date;
        try {
            date = LocalDateTime.parse(dateParameter);
        } catch (NumberFormatException ex) {
            ServletUtils
                    .writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                            JsonToExceptionConversor.toInputValidationException(new InputValidationException(
                                    "Invalid Request: " + "parameter 'date' is invalid '" + dateParameter + "'")),
                            null);

            return;
        }

        String inscriptionPriceParameter = req.getParameter("inscriptionPrice");
        if (inscriptionPriceParameter == null) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid Request: " + "parameter 'inscriptionPrice' is mandatory")),
                    null);
            return;
        }
        Double inscriptionPrice;
        try {
            inscriptionPrice = Double.parseDouble(inscriptionPriceParameter);
        } catch (NumberFormatException ex) {
            ServletUtils
                    .writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                            JsonToExceptionConversor.toInputValidationException(new InputValidationException(
                                    "Invalid Request: " + "parameter 'date' is invalid '" + inscriptionPriceParameter + "'")),
                            null);

            return;
        }

        String maxParticipantsParameter = req.getParameter("maxParticipants");
        if (maxParticipantsParameter == null) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException("Invalid Request: " + "parameter 'maxParticipants' is mandatory")),
                    null);
            return;
        }
        int maxParticipants;
        try {
            maxParticipants = Integer.parseInt(maxParticipantsParameter);
        } catch (NumberFormatException ex) {
            ServletUtils
                    .writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                            JsonToExceptionConversor.toInputValidationException(new InputValidationException(
                                    "Invalid Request: " + "parameter 'maxParticipants' is invalid '" + maxParticipantsParameter + "'")),
                            null);

            return;
        }
        
        /* Add race. */
        Long raceId;
        try {
            raceId = RaceServiceFactory.getService().addRace(new Race(city, description, inscriptionPrice, maxParticipants, 0, LocalDateTime.now(), date)).getRaceId();
        } catch (InputValidationException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(ex), null);
            return;
        }

        /* Return race. */
        Race race;
        try {
            race = RaceServiceFactory.getService().findRace(raceId);
        } catch (InstanceNotFoundException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    JsonToExceptionConversor.toInstanceNotFoundException(ex), null);
            return;
        }
        

        RestRaceDto raceDto = RaceToRestRaceDtoConversor.toRestRaceDto(race);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                JsonToRestRaceDtoConversor.toObjectNode(raceDto), null);
    }

}