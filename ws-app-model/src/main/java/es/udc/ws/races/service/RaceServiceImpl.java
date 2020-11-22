package es.udc.ws.races.service;

import es.udc.ws.races.model.inscription.SqlInscriptionDao;
import es.udc.ws.races.model.inscription.SqlInscriptionDaoFactory;
import es.udc.ws.races.model.race.Race;
import es.udc.ws.races.model.race.SqlRaceDao;
import es.udc.ws.races.model.race.SqlRaceDaoFactory;
import es.udc.ws.races.model.util.exceptions.InputValidationException;
import es.udc.ws.races.model.util.exceptions.InstanceNotFoundException;
import es.udc.ws.races.model.util.validation.PropertyValidator;
import es.udc.ws.util.sql.DataSourceLocator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static es.udc.ws.races.model.util.configuration.ModelConstants.*;

public class RaceServiceImpl implements RaceService{

    private final DataSource dataSource;
    private SqlRaceDao raceDao = null;
    private SqlInscriptionDao inscriptionDao = null;

    public RaceServiceImpl() {
        dataSource = DataSourceLocator.getDataSource(RACE_DATA_SOURCE);
        raceDao = SqlRaceDaoFactory.getDao();
        inscriptionDao = SqlInscriptionDaoFactory.getDao();
    }

    private void validateRace(Race race) throws InputValidationException {

        PropertyValidator.validateMandatoryString("city", race.getCity());
        PropertyValidator.validateMandatoryString("raceDescription", race.getRaceDescription());
        PropertyValidator.validateDouble("inscriptionPrice", race.getInscriptionPrice(), 0, MAX_INSCRIPTION_PRICE);
        PropertyValidator.validateInt("maxParticipants", race.getMaxParticipants(), 0, MAX_NUMBER_OF_PARTICIPANTS);

    }

    @Override
    public Race addRace(Race race) throws InputValidationException {
        return null;
    }

    @Override
    public Race findRace(Long raceId) throws InstanceNotFoundException {

        try (Connection connection = dataSource.getConnection()) {
            return raceDao.find(connection, raceId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
