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
import java.util.LinkedList;
import java.util.List;

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
        LocalDateTime celebrateDate = LocalDateTime.of(2019, 11, 22, 16, 00);
        return new Race("A Coruña", "Ejemplo de carrera pasada.", 20.50, 100, 50, creationDate, celebrateDate);
    }

    private Race futureRace() {
        LocalDateTime creationDate = LocalDateTime.now().withNano(0);
        LocalDateTime celebrationDate = LocalDateTime.of(2050, 11, 22, 16, 00);
        return new Race("A Coruña", "Ejemplo de carrera futura.", 20.50, 100, 50, creationDate, celebrationDate);
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

            /* After an inscription, the number of inscribed of race is increased, so we need to get the updated race. */
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

    /* Tests Caso de uso 5 (Alumno 2). */

    @Test
    public void testFindInscriptionByEmail() throws InputValidationException {

        /* Create list of valid inscriptions. */
        List<Inscription> inscriptions = new LinkedList<Inscription>();
        Race race1 = createRace(futureRace());
        Race race2 = createRace(futureRace());
        Race race3 = createRace(futureRace());
        Race race4 = createRace(futureRace());
        Long inscription1 = null;
        Long inscription2 = null;
        Long inscription3 = null;
        Long inscription4 = null;
        try {

            inscription1 = raceService.inscribeRace(race1.getRaceId(), VALID_USER_EMAIL, VALID_CREDIT_CARD_NUMBER);
            inscription2 = raceService.inscribeRace(race2.getRaceId(), VALID_USER_EMAIL, VALID_CREDIT_CARD_NUMBER);
            inscription3 = raceService.inscribeRace(race3.getRaceId(), VALID_USER_EMAIL, VALID_CREDIT_CARD_NUMBER);
            inscription4 = raceService.inscribeRace(race4.getRaceId(), "inscripcion.fuera@domain.com", "0123456789012345");
            inscriptions.add(raceService.findInscription(inscription1));
            inscriptions.add(raceService.findInscription(inscription2));
            inscriptions.add(raceService.findInscription(inscription3));

            List<Inscription> foundInscriptions = raceService.findInscriptionByUserEmail(VALID_USER_EMAIL);
            assertEquals(inscriptions, foundInscriptions);

            assertEquals(3, foundInscriptions.size());
            assertEquals(inscriptions.get(1), foundInscriptions.get(1));

        } catch (InputValidationException | InstanceNotFoundException | InscriptionDateOverException e) {
            throw new RuntimeException(e);
        } finally {
            // Clear Database
            removeInscription(inscription4);
            for (Inscription inscription : inscriptions) {
                removeInscription(inscription.getInscriptionId());
            }
            removeRace(race1.getRaceId());
            removeRace(race2.getRaceId());
            removeRace(race3.getRaceId());
            removeRace(race4.getRaceId());
        }
    }

    @Test
    public void testFindInscriptionByEmailWithInvalidEmail() {

        assertThrows(InputValidationException.class, () -> {
            List<Inscription> foundInscriptions = raceService.findInscriptionByUserEmail(INVALID_USER_EMAIL);
        });

    }

    @Test
    public void testFindInscriptionByEmailWithInvalidEmptyEmail() {

        assertThrows(InputValidationException.class, () -> {
            List<Inscription> foundInscriptions = raceService.findInscriptionByUserEmail(EMPTY_USER_EMAIL);
        });
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