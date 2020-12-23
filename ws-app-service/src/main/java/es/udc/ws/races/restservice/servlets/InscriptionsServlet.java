package es.udc.ws.races.restservice.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import es.udc.ws.races.restservice.dto.RestInscriptionDto;
import es.udc.ws.races.model.raceservice.RaceServiceFactory;
import es.udc.ws.races.model.raceservice.exceptions.AlreadyInscribedException;
import es.udc.ws.races.model.raceservice.exceptions.InscriptionDateOverException;
import es.udc.ws.races.model.raceservice.exceptions.MaxParticipantsException;
import es.udc.ws.races.model.inscription.Inscription;
import es.udc.ws.races.restservice.json.JsonToExceptionConversor;
import es.udc.ws.races.restservice.json.JsonToRestInscriptionDtoConversor;
import es.udc.ws.races.restservice.dto.InscriptionToRestInscriptionDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;

@SuppressWarnings("serial")
public class InscriptionsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}

