package es.udc.ws.races.test.model.raceservice;

import es.udc.ws.races.model.inscription.Inscription;
import es.udc.ws.races.model.inscription.SqlInscriptionDao;
import es.udc.ws.races.model.inscription.SqlInscriptionDaoFactory;
import es.udc.ws.races.model.race.Race;
import es.udc.ws.races.model.race.SqlRaceDao;
import es.udc.ws.races.model.race.SqlRaceDaoFactory;
import es.udc.ws.races.service.RaceService;
import es.udc.ws.races.service.RaceServiceFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;


import static es.udc.ws.races.model.util.configuration.ModelConstants.RACE_DATA_SOURCE;

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
                0, LocalDateTime.now().plusDays(3));
    }

    private Race createMovie(Race race) {

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

    private void updateSale(Inscription inscription) {

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
}

