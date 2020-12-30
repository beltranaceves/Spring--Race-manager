package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.model.raceservice.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class JsonToExceptionConversor {

    public static ObjectNode toInputValidationException(InputValidationException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "InputValidation");
        exceptionObject.put("message", ex.getMessage());

        return exceptionObject;
    }

    public static ObjectNode toInstanceNotFoundException(InstanceNotFoundException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
        ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "InstanceNotFound");
        exceptionObject.put("instanceId", (ex.getInstanceId() != null) ?
                ex.getInstanceId().toString() : null);
        exceptionObject.put("instanceType",
                ex.getInstanceType().substring(ex.getInstanceType().lastIndexOf('.') + 1));

        return exceptionObject;
    }

    public static ObjectNode toAlreadyInscribedException(AlreadyInscribedException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "AlreadyInscribed");
        exceptionObject.put("raceId", (ex.getRaceId() != null) ? ex.getRaceId() : null);
        if (ex.getUserEmail() != null) {
            exceptionObject.put("userEmail", ex.getUserEmail());
        } else {
            exceptionObject.set("userEmail", null);
        }

        return exceptionObject;
    }

    public static ObjectNode toInscriptionDateOverException(InscriptionDateOverException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "InscriptionDateOver");
        exceptionObject.put("raceId", (ex.getRaceId() != null) ? ex.getRaceId() : null);
        if (ex.getDateOver() != null) {
            exceptionObject.put("dateOver", ex.getDateOver().toString());
        } else {
            exceptionObject.set("dateOver", null);
        }

        return exceptionObject;
    }

    public static ObjectNode toMaxParticipantsException(MaxParticipantsException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "MaxParticipants");
        exceptionObject.put("raceId", (ex.getRaceId() != null) ? ex.getRaceId() : null);
        if (ex.getMaxParticipants() != 0) {
            exceptionObject.put("maxParticipants", ex.getMaxParticipants());
        } else {
            exceptionObject.set("maxParticipants", null);
        }

        return exceptionObject;
    }

    public static ObjectNode toDorsalAlreadyCollectedException(DorsalAlreadyCollectedException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "DorsalAlreadyCollected");
        exceptionObject.put("inscriptionId", (ex.getInscriptionID() != null) ? ex.getInscriptionID() : null);

        return exceptionObject;
    }

    public static ObjectNode toCreditCardDoesNotMatchException(CreditCardDoesNotMatchException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "CreditCardDoesNotMatch");
        exceptionObject.put("creditCard1", (ex.getCreditCard1() != null) ? ex.getCreditCard1() : null);
        exceptionObject.put("creditCard2", (ex.getCreditCard2() != null) ? ex.getCreditCard2() : null);

        return exceptionObject;
    }
}