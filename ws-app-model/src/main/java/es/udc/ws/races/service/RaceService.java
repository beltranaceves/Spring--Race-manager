package es.udc.ws.races.service;

import es.udc.ws.races.model.race.Race;
import es.udc.ws.races.model.util.exceptions.InputValidationException;
import es.udc.ws.races.model.util.exceptions.InstanceNotFoundException;

public interface RaceService {

    public Race addRace(Race race) throws InputValidationException;

    public Race findRace(Long raceId) throws InstanceNotFoundException;
}
