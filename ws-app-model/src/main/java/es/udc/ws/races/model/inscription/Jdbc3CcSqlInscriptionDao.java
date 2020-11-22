package es.udc.ws.races.model.inscription;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlInscriptionDao extends AbstractSqlInscriptionDao {

    @Override
    public Inscription create(Connection connection, Inscription inscription) {

        /* Create "queryString". */
        String queryString = "INSERT INTO Inscription"
                + " (userEmail, raceId, creditCardNumber, dorsalNumber, inscriptionDate, collected)"
                + " VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                        queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, inscription.getUserEmail());
            preparedStatement.setLong(i++, inscription.getRaceId());
            preparedStatement.setString(i++, inscription.getCreditCardNumber());
            preparedStatement.setInt(i++, inscription.getDorsalNumber());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(inscription.getInscriptionDate()));
            preparedStatement.setBoolean(i++, false);

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long inscriptionId = resultSet.getLong(1);

            /* Return inscription. */
            return new Inscription(inscriptionId, inscription.getUserEmail(), inscription.getRaceId(),
                    inscription.getCreditCardNumber(), inscription.getDorsalNumber(),
                    inscription.getInscriptionDate(), inscription.getCollected());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
