package es.udc.ws.races.model.inscription;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import es.udc.ws.races.model.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlInscriptionDao implements SqlInscriptionDao {

    protected AbstractSqlInscriptionDao() {
    }

    @Override
    public Inscription find(Connection connection, Long inputInscriptionId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT inscriptionId, userEmail, raceId,"
                + "creditCardNumber, dorsalNumber, inscriptionDate, collected FROM Inscription WHERE inscriptionId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, inputInscriptionId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(inputInscriptionId,
                        Inscription.class.getName());
            }

            /* Get results. */
            i = 1;
            Long inscriptionId = resultSet.getLong(i++);
            String userEmail = resultSet.getString(i++);
            Long raceId = resultSet.getLong(i++);
            String creditCardNumber = resultSet.getString(i++);
            int dorsalNumber = resultSet.getInt(i++);
			Timestamp inscriptionDateAsTimestamp = resultSet.getTimestamp(i++);
			LocalDateTime inscriptionDate = inscriptionDateAsTimestamp != null
					? inscriptionDateAsTimestamp.toLocalDateTime()
					: null;
	        Boolean collected = resultSet.getBoolean(i++);

            /* Return sale. */
            return new Inscription(inscriptionId, userEmail, raceId, creditCardNumber, dorsalNumber, inscriptionDate, collected);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Connection connection, Inscription inscription)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "UPDATE Inscription"
                + " SET inscriptionId = ?, userEmail = ?, raceId = ?, "
                + " creditCardNumber = ?, dorsalNumber = ?, inscriptionDate = ?, collected = ?, WHERE inscriptionId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, inscription.getInscriptionId());
            preparedStatement.setString(i++, inscription.getUserEmail());
            preparedStatement.setLong(i++, inscription.getRaceId());
            preparedStatement.setString(i++, inscription.getCreditCardNumber());
            preparedStatement.setInt(i++, inscription.getDorsalNumber());
			Timestamp date = inscription.getInscriptionDate() != null ? Timestamp.valueOf(inscription.getInscriptionDate()) : null;
            preparedStatement.setTimestamp(i++, date);
            preparedStatement.setBoolean(i++, inscription.getCollected());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(inscription.getInscriptionId(),
                        Inscription.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void remove(Connection connection, Long inscriptionId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM Inscription WHERE" + " inscriptionId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, inscriptionId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(inscriptionId,
                        Inscription.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    
    @Override
    public List<Inscription> findByUserEmail(Connection connection, String inputUserEmail) {

        /* Create "queryString". */
        String queryString = "SELECT inscriptionId, userEmail, raceId, "
                + " creditCardNumber, dorsalNumber, inscriptionDate, collected FROM Inscription WHERE userEmail=";
        queryString += inputUserEmail;
        queryString += " ORDER BY userEmail";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            /* Read movies. */
            List<Inscription> inscriptions = new ArrayList<Inscription>();

            while (resultSet.next()) {

                int i = 1;
                Long inscriptionId = Long.valueOf(resultSet.getLong(i++));
                String userEmail = resultSet.getString(i++);
                Long raceId = resultSet.getLong(i++);
                String creditCardNumber = resultSet.getString(i++);
                int dorsalNumber = resultSet.getInt(i++);
                Timestamp inscriptionDateAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime inscriptionDate = inscriptionDateAsTimestamp.toLocalDateTime();
                Boolean collected = resultSet.getBoolean(i++);
                inscriptions.add(new Inscription(inscriptionId, userEmail, raceId, creditCardNumber,
                        dorsalNumber, inscriptionDate, collected));
            }

            /* Return movies. */
            return inscriptions;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
