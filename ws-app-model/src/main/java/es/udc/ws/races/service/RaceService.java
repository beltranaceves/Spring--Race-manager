package es.udc.ws.races.service;

import es.udc.ws.races.model.inscription.Inscription;
import es.udc.ws.races.model.race.Race;
import es.udc.ws.races.model.util.exceptions.InputValidationException;
import es.udc.ws.races.model.util.exceptions.InstanceNotFoundException;

import java.util.List;

public interface RaceService {

    public Race addRace(Race race) throws InputValidationException;

    public Race findRace(Long raceId) throws InstanceNotFoundException;

    public List<Inscription> findInscriptionByUserEmail(String userEmail) throws InputValidationException;

    public Long inscribeRace(Long raceId, String userEmail, String creditCardNumber) throws InputValidationException, InstanceNotFoundException;
}
