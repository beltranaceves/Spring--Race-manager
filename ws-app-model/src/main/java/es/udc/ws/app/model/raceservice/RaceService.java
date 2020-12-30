package es.udc.ws.app.model.raceservice;

import es.udc.ws.app.model.race.Race;
import es.udc.ws.app.model.raceservice.exceptions.InscriptionDateOverException;
import es.udc.ws.app.model.raceservice.exceptions.dorsalAlreadyCollectedException;
import es.udc.ws.app.model.inscription.Inscription;
import es.udc.ws.app.model.raceservice.exceptions.AlreadyInscribedException;
import es.udc.ws.app.model.raceservice.exceptions.MaxParticipantsException;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface RaceService {

    public Race addRace(Race race) throws InputValidationException;

    public Race findRace(Long raceId) throws InstanceNotFoundException;

    public List<Race> findRacesByDateAndCity(LocalDateTime scheduleDate, String city) throws InputValidationException;

    public List<Inscription> findInscriptionByUserEmail(String userEmail) throws InputValidationException;

    public Inscription findInscription(Long inscriptionId) throws InstanceNotFoundException;

    public Long inscribeRace(Long raceId, String userEmail, String creditCardNumber) throws InputValidationException,
            InstanceNotFoundException, AlreadyInscribedException, InscriptionDateOverException, MaxParticipantsException;

    public int collectInscription(Long inscriptionId, String creditCardNumber) throws InstanceNotFoundException,
            dorsalAlreadyCollectedException, InputValidationException;

    public void removeRace(Long raceId) throws InstanceNotFoundException;

}
