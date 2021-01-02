package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestInscriptionDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.util.List;

public class JsonToRestInscriptionDtoConversor {

    public static ObjectNode toObjectNode(RestInscriptionDto inscription) {

        ObjectNode inscriptionNode = JsonNodeFactory.instance.objectNode();

        if (inscription.getInscriptionId() != null) {
            inscriptionNode.put("inscriptionId", inscription.getInscriptionId());
        }
        inscriptionNode.put("raceId", inscription.getRaceId()).
                put("dorsalNumber", inscription.getDorsalNumber()).
                put("collected", inscription.getCollected());
        
        return inscriptionNode;
    }

    public static ArrayNode toArrayNode(List<RestInscriptionDto> inscriptions) {

        ArrayNode inscriptionsNode = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < inscriptions.size(); i++) {
            RestInscriptionDto inscriptionDto = inscriptions.get(i);
            ObjectNode inscriptionObject = toObjectNode(inscriptionDto);
            inscriptionsNode.add(inscriptionObject);
        }

        return inscriptionsNode;
    }

    public static RestInscriptionDto toServiceInscriptionDto(InputStream inscription) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(inscription);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode inscriptionNode = (ObjectNode) rootNode;

                JsonNode inscriptionIdNode = inscriptionNode.get("inscriptionId");
                Long inscriptionId = (inscriptionIdNode != null) ? inscriptionIdNode.longValue() : null;
                Long raceId = inscriptionNode.get("raceId").longValue();
                int dorsalNumber = inscriptionNode.get("dorsalNumber").intValue();
                boolean collected = inscriptionNode.get("collected").booleanValue();

                return new RestInscriptionDto(inscriptionId, raceId, dorsalNumber, collected);
            }

        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ParsingException(ex);
        }
    }
}
