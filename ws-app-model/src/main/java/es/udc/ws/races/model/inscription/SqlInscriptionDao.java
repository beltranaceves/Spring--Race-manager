package es.udc.ws.races.model.inscription;

import java.sql.Connection;
import java.util.List;

import es.udc.ws.races.model.util.exceptions.InstanceNotFoundException;

public interface SqlInscriptionDao {

    public Inscription create(Connection connection, Inscription inscription);

    public void update(Connection connection, Inscription inscription)
            throws InstanceNotFoundException;
    
    public Inscription find(Connection connection, Long inscriptionId)
            throws InstanceNotFoundException;

    public List<Inscription> findByUserEmail(Connection connection,
            String userEmail);

    public void remove(Connection connection, Long inscriptionId)
            throws InstanceNotFoundException;
}

