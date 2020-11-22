package es.udc.ws.races.service;

import es.udc.ws.races.model.inscription.SqlInscriptionDao;
import es.udc.ws.races.model.inscription.SqlInscriptionDaoFactory;
import es.udc.ws.races.model.race.Race;
import es.udc.ws.races.model.race.SqlRaceDao;
import es.udc.ws.races.model.race.SqlRaceDaoFactory;
import es.udc.ws.races.model.inscription.Inscription;
import es.udc.ws.races.model.util.exceptions.InputValidationException;
import es.udc.ws.races.model.util.exceptions.InstanceNotFoundException;
import es.udc.ws.races.model.util.exceptions.InscriptionDateOverException;
import es.udc.ws.races.model.util.validation.PropertyValidator;
import es.udc.ws.util.sql.DataSourceLocator;

import static es.udc.ws.races.model.util.configuration.ModelConstants.RACE_DATA_SOURCE;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

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
        validateRace(race);
        race.setCreationDate(LocalDateTime.now());

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Race createdRace = raceDao.create(connection, race);

                /* Commit. */
                connection.commit();

                return createdRace;

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Race findRace(Long raceId) throws InstanceNotFoundException {

        try (Connection connection = dataSource.getConnection()) {
            return raceDao.find(connection, raceId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Race> findRacesByDate(LocalDateTime scheduleDate) {

        try (Connection connection = dataSource.getConnection()) {
            return raceDao.findByDate(connection, scheduleDate);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Race> findRacesByDateAndCity(LocalDateTime scheduleDate, String city) {

        try (Connection connection = dataSource.getConnection()) {
            return raceDao.findByDateAndCity(connection, scheduleDate, city);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateRace(Race race) throws InputValidationException, InstanceNotFoundException {

        validateRace(race);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                raceDao.update(connection, race);

                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Long inscribeRace(Long raceId, String userEmail, String creditCardNumber)
            throws InstanceNotFoundException, InputValidationException {

        /* Validate creditCard and userEmail. */
        PropertyValidator.validateCreditCard(creditCardNumber);
        PropertyValidator.validateUserEmail(userEmail);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Find race, set inscription create date and check if it is valid. */
                Race race = raceDao.find(connection, raceId);
                LocalDateTime inscriptionDate = LocalDateTime.now();
                /* Calculate last date to inscribe. */
                LocalDateTime dateOver = race.getScheduleDate().minusHours(24);

                if ( inscriptionDate.isAfter(dateOver) ) {
                    throw new InscriptionDateOverException(raceId, dateOver);
                } else {
                    int dorsalNumber = race.getNumberOfInscribed() + 1;
                    Inscription inscription = inscriptionDao.create(connection, new Inscription(userEmail, raceId, creditCardNumber, dorsalNumber,
                            inscriptionDate, false));

                    /* Commit to create inscription. */
                    connection.commit();

                    /* Change numberOfInscribed of the race and then updateRace. */
                    race.setNumberOfInscribed(race.getNumberOfInscribed() + 1);
                    updateRace(race);

                    /* Commit to update race. */
                    connection.commit();

                    return inscription.getInscriptionId();
                }

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            } catch (InscriptionDateOverException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException | InscriptionDateOverException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Inscription> findInscriptionByUserEmail(String userEmail) throws InputValidationException {

        /* Validate email. */
        PropertyValidator.validateUserEmail(userEmail);

        try (Connection connection = dataSource.getConnection()) {
            return inscriptionDao.findByUserEmail(connection, userEmail);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeRace(Long raceId) throws InstanceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            raceDao.remove(connection, raceId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
