package es.udc.ws.races.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.races.model.raceservice.exceptions.AlreadyInscribedException;
import es.udc.ws.races.model.raceservice.exceptions.InscriptionDateOverException;
import es.udc.ws.races.model.raceservice.exceptions.MaxParticipantsException;

public class JsonToExceptionConversor {

    public static ObjectNode toInputValidationException(InputValidationException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        return exceptionObject;
    }

    public static ObjectNode toInstanceNotFoundException(InstanceNotFoundException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        return exceptionObject;
    }

    public static ObjectNode toAlreadyInscribedException(AlreadyInscribedException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        return exceptionObject;
    }

    public static ObjectNode toInscriptionDateOverException(InscriptionDateOverException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        return exceptionObject;
    }

    public static ObjectNode toMaxParticipantsException(MaxParticipantsException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        return exceptionObject;
    }
}

