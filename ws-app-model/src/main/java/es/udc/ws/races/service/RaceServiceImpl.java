package es.udc.ws.races.service;

import es.udc.ws.races.model.inscription.SqlInscriptionDao;
import es.udc.ws.races.model.inscription.SqlInscriptionDaoFactory;
import es.udc.ws.races.model.race.Race;
import es.udc.ws.races.model.race.SqlRaceDao;
import es.udc.ws.races.model.race.SqlRaceDaoFactory;
import es.udc.ws.races.model.inscription.Inscription;
import es.udc.ws.races.model.util.exceptions.*;
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

    private void validateInscription(Inscription inscription) throws InputValidationException {

        PropertyValidator.validateCreditCard(inscription.getCreditCardNumber());
        PropertyValidator.validateLong("raceId", inscription.getRaceId(), 0, MAX_RACE_NUMBER);
        PropertyValidator.validateUserEmail(inscription.getUserEmail());
        PropertyValidator.validateInt("dorsalNumber",inscription.getDorsalNumber(), 0 , MAX_DORSAL_NUMBER);

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
            throws InstanceNotFoundException, InputValidationException, InscriptionDateOverException {

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

                if (inscriptionDate.isAfter(dateOver)) {
                    throw new InscriptionDateOverException(raceId, dateOver);
                } else {
                    int dorsalNumber = race.getNumberOfInscribed() + 1;
                    Boolean collected = false;
                    Inscription inscription = inscriptionDao.create(connection, new Inscription(userEmail, raceId, creditCardNumber, dorsalNumber,
                            inscriptionDate, collected));

                    /* Change numberOfInscribed of the race and then update it. */
                    race.setNumberOfInscribed(race.getNumberOfInscribed() + 1);
                    raceDao.update(connection, race);

                    /* Commit to create inscription. */
                    connection.commit();

                    return inscription.getInscriptionId();
                }

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (InscriptionDateOverException e) {
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
    public Inscription findInscription(Long inscriptionId) throws InstanceNotFoundException {

        try (Connection connection = dataSource.getConnection()) {
            return inscriptionDao.find(connection, inscriptionId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateInscription(Inscription inscription) throws InputValidationException, InstanceNotFoundException {

        validateInscription(inscription);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                inscriptionDao.update(connection, inscription);

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
    public int collectInscription(Long inscriptionId, String creditCardNumber) throws InstanceNotFoundException,
            dorsalAlreadyCollectedException, InputValidationException {

        try (Connection connection = dataSource.getConnection()) {

            Inscription inscription = inscriptionDao.find(connection, inscriptionId);
            LocalDateTime now = LocalDateTime.now();
            Race race = findRace(inscription.getRaceId());

            if (inscription.getCollected()) {
                throw new dorsalAlreadyCollectedException();
            } else {
                if (race.getScheduleDate().isAfter(now)) {
                    inscription.setCollected(true);
                    updateInscription(inscription);
                    return inscription.getDorsalNumber();
                } else {
                    throw new InscriptionExpirationException(inscription.getDorsalNumber(), race.getScheduleDate());
                }
            }

        } catch (SQLException | InputValidationException | InscriptionExpirationException e) {
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