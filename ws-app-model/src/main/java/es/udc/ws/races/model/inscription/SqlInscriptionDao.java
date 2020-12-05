package es.udc.ws.races.model.inscription;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.util.List;


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

