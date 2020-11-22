package es.udc.ws.races.model.race;

import es.udc.ws.races.model.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;

public interface SqlRaceDao {

    public Race find(Connection connection, Long raceId)
            throws InstanceNotFoundException;

    public void update(Connection connection, Race race)
            throws InstanceNotFoundException;

}