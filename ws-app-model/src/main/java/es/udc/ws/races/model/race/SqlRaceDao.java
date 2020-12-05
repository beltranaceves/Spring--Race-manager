package es.udc.ws.races.model.race;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public interface SqlRaceDao {

    public Race create(Connection connection, Race race);


    public Race find(Connection connection, Long raceId)
            throws InstanceNotFoundException;

    public List<Race> findByDate(Connection connection,
                                 LocalDateTime creationDate);

    public List<Race> findByDateAndCity(Connection connection,
                                        LocalDateTime creationDate, String city);

    public void update(Connection connection, Race race)
            throws InstanceNotFoundException;

    public void remove(Connection connection, Long raceId)
            throws InstanceNotFoundException;
}