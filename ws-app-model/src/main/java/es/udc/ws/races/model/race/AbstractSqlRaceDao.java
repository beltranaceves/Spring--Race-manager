package es.udc.ws.races.model.race;

import es.udc.ws.races.model.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlRaceDao implements SqlRaceDao {

    protected AbstractSqlRaceDao() {}

    @Override
    public Race find(Connection connection, Long inputRaceId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT raceId, city, raceDescription,"
                + "inscriptionPrice, maxParticipants, creationDate, sheduleDate, numberOfInscribed FROM Race WHERE raceId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, inputRaceId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(inputRaceId,
                        Race.class.getName());
            }

            /* Get results. */
            i = 1;
            Long raceId = resultSet.getLong(i++);
            String city = resultSet.getString(i++);
            String raceDescription = resultSet.getString(i++);
            double inscriptionPrice = resultSet.getDouble(i++);
            int maxParticipants = resultSet.getInt(i++);
            Timestamp creationDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime creationDate = creationDateAsTimestamp != null
                    ? creationDateAsTimestamp.toLocalDateTime()
                    : null;
            Timestamp scheduleDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime scheduleDate = scheduleDateAsTimestamp != null
                    ? scheduleDateAsTimestamp.toLocalDateTime()
                    : null;
            int numberOfInscribed = resultSet.getInt(i++);

            /* Return race. */
            return new Race(raceId, city, raceDescription, inscriptionPrice, maxParticipants, numberOfInscribed, creationDate, scheduleDate);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Connection connection, Race race)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "UPDATE Race"
                + " SET raceId = ?, userEmail = ?, raceId = ?, "
                + " creditCardNumber = ?, dorsalNumber = ?, raceDate = ?, scheduleDate = ?, collected = ?, WHERE raceId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, race.getRaceId());
            preparedStatement.setString(i++, race.getCity());
            preparedStatement.setString(i++, race.getRaceDescription());
            preparedStatement.setDouble(i++, race.getInscriptionPrice());
            preparedStatement.setInt(i++, race.getMaxParticipants());
            Timestamp date = race.getCreationDate() != null ? Timestamp.valueOf(race.getCreationDate()) : null;
            preparedStatement.setTimestamp(i++, date);
            Timestamp scheduleDate = race.getScheduleDate() != null ? Timestamp.valueOf(race.getScheduleDate()) : null;
            preparedStatement.setTimestamp(i++, scheduleDate);
            preparedStatement.setInt(i++, race.getNumberOfInscribed());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(race.getRaceId(),
                        Race.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long raceId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM Race WHERE" + " raceId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, raceId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(raceId,
                        Race.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}