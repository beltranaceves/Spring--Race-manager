package es.udc.ws.app.model.raceservice;

import es.udc.ws.app.model.race.Race;
import es.udc.ws.app.model.race.SqlRaceDao;
import es.udc.ws.app.model.race.SqlRaceDaoFactory;
import es.udc.ws.app.model.raceservice.exceptions.*;
import es.udc.ws.app.model.inscription.SqlInscriptionDao;
import es.udc.ws.app.model.inscription.SqlInscriptionDaoFactory;
import es.udc.ws.app.model.inscription.Inscription;
import es.udc.ws.app.model.util.PropertyValidatorAditional;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;

import static es.udc.ws.app.model.util.ModelConstants.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

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
        PropertyValidatorAditional.validateInt("maxParticipants", race.getMaxParticipants(), 0, MAX_NUMBER_OF_PARTICIPANTS);

    }

    private void validateInscription(Inscription inscription) throws InputValidationException {

        PropertyValidator.validateCreditCard(inscription.getCreditCardNumber());
        PropertyValidator.validateLong("raceId", inscription.getRaceId(), 0, MAX_RACE_NUMBER);
        PropertyValidatorAditional.validateUserEmail(inscription.getUserEmail());
        PropertyValidatorAditional.validateInt("dorsalNumber",inscription.getDorsalNumber(), 0 , MAX_DORSAL_NUMBER);

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
            throws InstanceNotFoundException, InputValidationException, AlreadyInscribedException,
            InscriptionDateOverException, MaxParticipantsException {

        /* Validate creditCard and userEmail. */
        PropertyValidator.validateCreditCard(creditCardNumber);
        PropertyValidatorAditional.validateUserEmail(userEmail);

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

                if (inscriptionDao.alreadyInscribed(connection, race.getRaceId(), userEmail)) {
                    throw new AlreadyInscribedException(raceId, userEmail);
                }
                if (inscriptionDate.isAfter(dateOver)) {
                    throw new InscriptionDateOverException(raceId, dateOver);
                }
                if (race.getNumberOfInscribed() == race.getMaxParticipants()) {
                    throw new MaxParticipantsException(race.getRaceId(), race.getMaxParticipants());
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

            } catch (AlreadyInscribedException e) {
                connection.commit();
                throw e;
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (InscriptionDateOverException e) {
                connection.commit();
                throw e;
            } catch (MaxParticipantsException e) {
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
            DorsalAlreadyCollectedException, InputValidationException, CreditCardDoesNotMatchException {

        //validateInscription(inscription);

        try (Connection connection = dataSource.getConnection()) {

            Inscription inscription = inscriptionDao.find(connection, inscriptionId);
            LocalDateTime now = LocalDateTime.now();
            Race race = raceDao.find(connection, inscription.getRaceId());

            validateInscription(inscription);

            if (!inscription.getCreditCardNumber().contentEquals(creditCardNumber)) {
                throw new CreditCardDoesNotMatchException(creditCardNumber, inscription.getCreditCardNumber());
            }
            if (inscription.getCollected()) {
                throw new DorsalAlreadyCollectedException(inscriptionId);
            } else {
                inscription.setCollected(true);
                inscriptionDao.update(connection, inscription);
                return inscription.getDorsalNumber();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Inscription> findInscriptionByUserEmail(String userEmail) throws InputValidationException {

        /* Validate email. */
        PropertyValidatorAditional.validateUserEmail(userEmail);

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