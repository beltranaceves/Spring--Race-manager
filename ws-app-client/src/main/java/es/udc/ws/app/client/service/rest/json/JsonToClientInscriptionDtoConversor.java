package es.udc.ws.app.client.service.rest.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.app.client.service.dto.ClientInscriptionDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonToClientInscriptionDtoConversor {

    public static ClientInscriptionDto toClientInscriptionDto(InputStream jsonInscription) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonInscription);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode inscriptionObject = (ObjectNode) rootNode;

                JsonNode inscriptionIdNode = inscriptionObject.get("inscriptionId");
                Long inscriptionId = (inscriptionIdNode != null) ? inscriptionIdNode.longValue() : null;

                Long raceId = inscriptionObject.get("raceId").longValue();
                int dorsalNumber = inscriptionObject.get("dorsalNumber").intValue();
                Boolean collected = inscriptionObject.get("collected").booleanValue();

                return new ClientInscriptionDto(inscriptionId, raceId, dorsalNumber, collected);

            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientInscriptionDto> toClientInscriptionDtos(InputStream jsonInscriptions) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonInscriptions);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode inscriptionsArray = (ArrayNode) rootNode;
                List<ClientInscriptionDto> inscriptionDtos = new ArrayList<>(inscriptionsArray.size());
                for (JsonNode inscriptionNode : inscriptionsArray) {
                    inscriptionDtos.add(toClientInscriptionDto(inscriptionNode));
                }

                return inscriptionDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientInscriptionDto toClientInscriptionDto(JsonNode inscriptionNode) throws ParsingException {
        if (inscriptionNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode inscriptionObject = (ObjectNode) inscriptionNode;

            JsonNode inscriptionIdNode = inscriptionObject.get("inscriptionId");
            Long inscriptionId = (inscriptionIdNode != null) ? inscriptionIdNode.longValue() : null;

            Long raceId = inscriptionObject.get("raceId").longValue();
            int dorsalNumber = inscriptionObject.get("dorsalNumber").intValue();
            Boolean collected = inscriptionObject.get("collected").booleanValue();

            return new ClientInscriptionDto(inscriptionId, raceId, dorsalNumber, collected);
        }
    }

    public static ObjectNode toObjectNode(ClientInscriptionDto inscription) throws IOException {

        ObjectNode inscriptionObject = JsonNodeFactory.instance.objectNode();

        if (inscription.getInscriptionId() != null) {
            inscriptionObject.put("inscriptionId", inscription.getInscriptionId());
        }
        inscriptionObject.put("creditCardNumber", inscription.getCreditCardNumber());

        return inscriptionObject;
    }
}

