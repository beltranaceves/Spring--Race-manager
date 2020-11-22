package es.udc.ws.races.model.race;

import java.sql.*;

public class Jdbc3CcSqlRaceDao extends AbstractSqlRaceDao {

    @Override
    public Race create(Connection connection, Race race) {

        /* Create "queryString". */
        String queryString = "INSERT INTO Race"
                + " (city, raceDescription, inscriptionPrice, maxParticipants, creationDate, scheduleDate, numberOfInscribed)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
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
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long raceId = resultSet.getLong(1);

            /* Return race. */
            return new Race(raceId, race.getCity(), race.getRaceDescription(),
                    race.getInscriptionPrice(), race.getMaxParticipants(), race.getNumberOfInscribed(),
                    race.getCreationDate(), race.getScheduleDate());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch(Exception e) {
            return null;
        }
    }

}
