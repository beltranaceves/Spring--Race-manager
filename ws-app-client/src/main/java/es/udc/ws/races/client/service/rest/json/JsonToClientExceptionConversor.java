package es.udc.ws.races.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import es.udc.ws.races.client.service.exceptions.ClientAlreadyInscribedException;
import es.udc.ws.races.client.service.exceptions.ClientInscriptionDateOverException;
import es.udc.ws.races.client.service.exceptions.ClientMaxParticipantsException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;
import org.apache.http.HttpStatus;

import java.io.InputStream;
import java.time.LocalDateTime;

public class JsonToClientExceptionConversor {

    public static Exception fromBadRequestErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                switch (errorType) {

                    case "InputValidation":
                        return toInputValidationException(rootNode);

                    case "AlreadyInscribed":
                        return toAlreadyInscribedException(rootNode);

                    case "InscriptionDateOver":
                        return toInscriptionDateOverException(rootNode);

                    case "MaxParticipants":
                        return toMaxParticipantsException(rootNode);

                    default:
                        throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InputValidationException toInputValidationException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InputValidationException(message);
    }

    private static ClientAlreadyInscribedException toAlreadyInscribedException(JsonNode rootNode) {
        Long saleId = rootNode.get("raceId").longValue();
        String userEmail = rootNode.get("userEmail").textValue();

        return new ClientAlreadyInscribedException(saleId, userEmail);
    }

    private static ClientInscriptionDateOverException toInscriptionDateOverException(JsonNode rootNode) {
        Long saleId = rootNode.get("raceId").longValue();
        String dateOverAsString = rootNode.get("dateOver").textValue();
        LocalDateTime dateOver = null;

        if (dateOverAsString != null) {
            dateOver = LocalDateTime.parse(dateOverAsString);
        }

        return new ClientInscriptionDateOverException(saleId, dateOver);
    }

    private static ClientMaxParticipantsException toMaxParticipantsException(JsonNode rootNode) {
        Long saleId = rootNode.get("raceId").longValue();
        int maxParticipants = rootNode.get("maxParticipants").intValue();

        return new ClientMaxParticipantsException(saleId, maxParticipants);
    }

    public static Exception fromNotFoundErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InstanceNotFound")) {
                    return toInstanceNotFoundException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) {
        String instanceId = rootNode.get("instanceId").textValue();
        String instanceType = rootNode.get("instanceType").textValue();
        return new InstanceNotFoundException(instanceId, instanceType);
    }

}

