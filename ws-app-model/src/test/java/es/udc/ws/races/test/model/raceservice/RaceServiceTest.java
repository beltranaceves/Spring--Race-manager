package es.udc.ws.races.test.model.raceservice;

import es.udc.ws.races.model.inscription.Inscription;
import es.udc.ws.races.model.inscription.SqlInscriptionDao;
import es.udc.ws.races.model.inscription.SqlInscriptionDaoFactory;
import es.udc.ws.races.model.race.Race;
import es.udc.ws.races.model.race.SqlRaceDao;
import es.udc.ws.races.model.race.SqlRaceDaoFactory;
import es.udc.ws.races.model.util.exceptions.InscriptionDateOverException;
import es.udc.ws.races.model.util.exceptions.dorsalAlreadyCollectedException;
import es.udc.ws.races.service.RaceService;
import es.udc.ws.races.service.RaceServiceFactory;
import es.udc.ws.races.model.util.exceptions.InputValidationException;
import es.udc.ws.races.model.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;


import static es.udc.ws.races.model.util.configuration.ModelConstants.RACE_DATA_SOURCE;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class RaceServiceTest {

    private final long NON_EXISTENT_RACE_ID = -1;
    private final long NON_EXISTENT_INSCRIPTION_ID = -1;
    private final String USER_ID = "ws-user";

    private final String VALID_CREDIT_CARD_NUMBER = "1234567890123456";
    private final String INVALID_CREDIT_CARD_NUMBER = "";

    private final String VALID_USER_EMAIL = "test.mail@gmail.com";
    private final String INVALID_USER_EMAIL = "test.mail@@mal.com";
    private final String EMPTY_USER_EMAIL = "";

    private static RaceService raceService = null;

    private static SqlRaceDao raceDao = null;
    private static SqlInscriptionDao inscriptionDao = null;

    @BeforeAll
    public static void init() {

        /*
         * Create a simple data source and add it to "DataSourceLocator" (this
         * is needed to test "es.udc.ws.movies.model.races.service.RaceService"
         */
        DataSource dataSource = new SimpleDataSource();

        /* Add "dataSource" to "DataSourceLocator". */
        DataSourceLocator.addDataSource(RACE_DATA_SOURCE, dataSource);

        raceService = RaceServiceFactory.getService();
        raceDao = SqlRaceDaoFactory.getDao();

        inscriptionDao = SqlInscriptionDaoFactory.getDao();
    }

    private Race getValidRace(String city, Long offset) {
        return new Race(city, "Carrera de ejemplo", 30.0, 150,
                0, LocalDateTime.now().plusDays(3).withNano(0));
    }

    private Race pastRace() {
        LocalDateTime creationDate = LocalDateTime.now().withNano(0);
        LocalDateTime celebrateDate = LocalDateTime.of(2020, 11, 22, 16, 00);
        return new Race("Coruña", "Ejemplo de carrera pasada", 20.50, 100, 50, creationDate, celebrateDate);
    }

    private Race futureRace() {
        LocalDateTime creationDate = LocalDateTime.now().withNano(0);
        LocalDateTime celebrationDate = LocalDateTime.of(2050, 11, 22, 16, 00);
        return new Race("Coruña", "Ejemplo de carrera futura", 20.50, 100, 50, creationDate, celebrationDate);
    }

    private Race createRace(Race race) {

        Race addedRace = null;
        try {
            addedRace = raceService.addRace(race);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return addedRace;
    }

    private void removeRace(Long raceId) {

        try {
            raceService.removeRace(raceId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void removeInscription(Long inscriptionId) {

        DataSource dataSource = DataSourceLocator.getDataSource(RACE_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                inscriptionDao.remove(connection, inscriptionId);

                /* Commit. */
                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            } catch (es.udc.ws.races.model.util.exceptions.InstanceNotFoundException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }

    private void updateInscription(Inscription inscription) {

        DataSource dataSource = DataSourceLocator.getDataSource(RACE_DATA_SOURCE);

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
                throw new RuntimeException(e);
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

    /* Tests Caso de uso 4 (Alumno 2). */
    @Test
    public void testValidInscribeRace() throws InputValidationException, InstanceNotFoundException, InscriptionDateOverException {

        Race race = createRace(futureRace());
        Long inscription = null;
        try {

            LocalDateTime beforeInscribeDate = LocalDateTime.now().withNano(0);
            inscription = raceService.inscribeRace(race.getRaceId(), VALID_USER_EMAIL, VALID_CREDIT_CARD_NUMBER);

            LocalDateTime afterInscribeDate = LocalDateTime.now().withNano(0);

            // Find inscription
            Inscription foundInscription = raceService.findInscription(inscription);

            race = raceService.findRace(race.getRaceId());
            // Check inscription
            assertEquals(inscription, foundInscription.getInscriptionId());
            assertEquals(VALID_CREDIT_CARD_NUMBER, foundInscription.getCreditCardNumber());
            assertEquals(VALID_USER_EMAIL, foundInscription.getUserEmail());
            assertEquals(race.getRaceId(), foundInscription.getRaceId());
            assertTrue((foundInscription.getInscriptionDate().compareTo(beforeInscribeDate) >= 0)
                    && (foundInscription.getInscriptionDate().compareTo(afterInscribeDate) <= 0));
            assertFalse(foundInscription.getCollected());
            assertEquals(race.getNumberOfInscribed(), foundInscription.getDorsalNumber());
        } finally {
            if (inscription != null) {
                removeInscription(inscription);
            }
            removeRace(race.getRaceId());
        }
    }

    @Test
    public void testInscribeRaceWithInvalidDate() {

        Race race = createRace(pastRace());
        try {
            assertThrows(InscriptionDateOverException.class, () -> {
                Long inscription = raceService.inscribeRace(race.getRaceId(), VALID_USER_EMAIL, VALID_CREDIT_CARD_NUMBER);
                removeInscription(inscription);
            });
        } finally {
            // Clear database
            removeRace(race.getRaceId());
        }
    }

    @Test
    public void testInscribeNonExistentRace() {

        assertThrows(InstanceNotFoundException.class, () -> {
            Long inscription = raceService.inscribeRace(NON_EXISTENT_RACE_ID, VALID_USER_EMAIL, VALID_CREDIT_CARD_NUMBER);
            removeInscription(inscription);
        });

    }

    @Test
    public void testInscribeRaceWithInvalidEmail() {
        Race race = createRace(futureRace());
        try {
            assertThrows(InputValidationException.class, () -> {
                Long inscription = raceService.inscribeRace(race.getRaceId(), INVALID_USER_EMAIL, VALID_CREDIT_CARD_NUMBER);
                removeInscription(inscription);
            });
        } finally {
            // Clear database
            removeRace(race.getRaceId());
        }
    }

    @Test
    public void testInscribeRaceWithInvalidEmptyEmail() {
        Race race = createRace(futureRace());
        try {
            assertThrows(InputValidationException.class, () -> {
                Long inscription = raceService.inscribeRace(race.getRaceId(), EMPTY_USER_EMAIL, VALID_CREDIT_CARD_NUMBER);
                removeInscription(inscription);
            });
        } finally {
            // Clear database
            removeRace(race.getRaceId());
        }
    }

    @Test
    public void testInscribeRaceWithInvalidEmptyCreditCard() {
        Race race = createRace(futureRace());
        try {
            assertThrows(InputValidationException.class, () -> {
                Long inscription = raceService.inscribeRace(race.getRaceId(), VALID_USER_EMAIL, INVALID_CREDIT_CARD_NUMBER);
                removeInscription(inscription);
            });
        } finally {
            // Clear database
            removeRace(race.getRaceId());
        }
    }

    //Parte alumno 3 (apartado 2)
    @Test
    public void testAddMovieAndFindMovie() throws InputValidationException, InstanceNotFoundException {

        Race race = getValidRace("A Coruña", (long) 1);
        Race addedRace = null;

        try {

            // Add Race
            addedRace = createRace(race);

            // Find Race
            Race foundRace = raceService.findRace(addedRace.getRaceId());

            assertEquals(foundRace.getRaceId(), addedRace.getRaceId());
            assertEquals(foundRace.getCity(), addedRace.getCity());
            assertEquals(foundRace.getRaceDescription(), addedRace.getRaceDescription());
            assertEquals(foundRace.getCreationDate(), addedRace.getCreationDate());
            assertEquals(foundRace.getMaxParticipants(), addedRace.getMaxParticipants());
            assertEquals(foundRace.getScheduleDate(), addedRace.getScheduleDate());
            assertEquals(foundRace.getInscriptionPrice(), addedRace.getInscriptionPrice());
            assertEquals(foundRace.getNumberOfInscribed(), addedRace.getNumberOfInscribed());

        } catch (InstanceNotFoundException e) {
            e.printStackTrace();

        } finally {
            // Clear Database
            if (addedRace!=null) {
                removeRace(addedRace.getRaceId());
            }
        }
    }

    //Parte alumno 3 (caso 6)
    @Test
    public void testCollectDorsal() throws InputValidationException, InstanceNotFoundException {

        Race race = getValidRace("A Coruña", (long) 1);
        Race addedRace = null;
        Race foundRace = null;
        Long addedInscription = null;

        try {

            // Add Race
            addedRace = createRace(race);

            // Find Race
            foundRace = raceService.findRace(addedRace.getRaceId());

            // Add Inscription
            addedInscription = raceService.inscribeRace(foundRace.getRaceId(), "user@domain.com", VALID_CREDIT_CARD_NUMBER);

            foundRace = raceService.findRace(addedRace.getRaceId());
            int dorsalNumber = raceService.collectInscription(addedInscription, VALID_CREDIT_CARD_NUMBER);

            assertEquals(foundRace.getNumberOfInscribed(), dorsalNumber);

        } catch (InputValidationException e) {
            e.printStackTrace();
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        } catch (InscriptionDateOverException e) {
            e.printStackTrace();
        } catch (dorsalAlreadyCollectedException e) {
            e.printStackTrace();
        } finally {
            // Clear Database
            if (addedRace!=null) {
                removeRace(addedRace.getRaceId());
            }
        }
    }


    //Parte alumno 3 (caso 6)
    @Test
    public void testCollectDorsalDorsalAlreadyCollected() throws InputValidationException, InstanceNotFoundException {

        assertThrows(dorsalAlreadyCollectedException.class, () -> {
            Race race = getValidRace("A Coruña", (long) 1);
            Race addedRace = null;
            Race foundRace = null;
            Long addedInscription = null;

            try {

                // Add Race
                addedRace = createRace(race);

                // Find Race
                foundRace = raceService.findRace(addedRace.getRaceId());

                // Add Inscription
                addedInscription = raceService.inscribeRace(foundRace.getRaceId(), "user@domain.com", VALID_CREDIT_CARD_NUMBER);

                foundRace = raceService.findRace(addedRace.getRaceId());
                int dorsalNumber = raceService.collectInscription(addedInscription, VALID_CREDIT_CARD_NUMBER);
                dorsalNumber = raceService.collectInscription(addedInscription, VALID_CREDIT_CARD_NUMBER);

            } catch (InputValidationException e) {
                e.printStackTrace();
            } catch (InstanceNotFoundException e) {
                e.printStackTrace();
            } catch (InscriptionDateOverException e) {
                e.printStackTrace();
            } finally {
                // Clear Database
                if (addedRace!=null) {
                    removeRace(addedRace.getRaceId());
                }
            }
        });

    }
}