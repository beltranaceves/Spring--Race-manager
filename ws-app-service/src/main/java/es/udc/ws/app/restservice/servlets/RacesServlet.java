package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.race.Race;
import es.udc.ws.app.model.raceservice.RaceServiceFactory;
import es.udc.ws.app.restservice.dto.RaceToRestRaceDtoConversor;
import es.udc.ws.app.restservice.json.JsonToExceptionConversor;
import es.udc.ws.app.restservice.json.JsonToRestRaceDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.exceptions.ParsingException;
import es.udc.ws.util.servlet.ServletUtils;
import es.udc.ws.app.restservice.dto.RestRaceDto;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class RacesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());
        if (path == null || path.length() == 0) {
            String raceId, city;
            raceId = req.getParameter("raceId");
            if(raceId == null) {
                String date = req.getParameter("date").split("T")[0];
                List<Race> races = null;
                city = req.getParameter("city");
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDateTime dateTime = LocalDate.parse(date, formatter).atStartOfDay();

                    races = RaceServiceFactory.getService().findRacesByDateAndCity(dateTime, city);
                } catch (InputValidationException e) {
                    e.printStackTrace();
                }
                List<RestRaceDto> racesDto = RaceToRestRaceDtoConversor.toRestRaceDtosComplete(races);
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                        JsonToRestRaceDtoConversor.toArrayNodeComplete(racesDto), null);
            } else {
                //FIND BY RACEID
                Race race;
                try{
                    race = RaceServiceFactory.getService().findRace(Long.parseLong(req.getParameter("raceId")));
                } catch (InstanceNotFoundException ex) {
                    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                            JsonToExceptionConversor.toInstanceNotFoundException(ex), null);
                    return;
                }
                RestRaceDto raceDto = RaceToRestRaceDtoConversor.toRestRaceDto(race);
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                        JsonToRestRaceDtoConversor.toObjectNode(raceDto), null);
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
        RestRaceDto raceDto;
        try {
            raceDto = JsonToRestRaceDtoConversor.toServiceRaceDto(req.getInputStream());
        } catch (ParsingException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, JsonToExceptionConversor
                    .toInputValidationException(new InputValidationException(ex.getMessage())), null);
            return;
        }
        Race race = RaceToRestRaceDtoConversor.toRace(raceDto);
        try {
            race = RaceServiceFactory.getService().addRace(race);
        } catch (InputValidationException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(ex), null);
            return;
        }
        raceDto = RaceToRestRaceDtoConversor.toRestRaceDto(race);

        String movieURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + race.getRaceId();
        Map<String, String> headers = new HashMap<>(1);
        headers.put("raceId", race.getRaceId().toString());

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                JsonToRestRaceDtoConversor.toObjectNode(raceDto), headers);
    }

}