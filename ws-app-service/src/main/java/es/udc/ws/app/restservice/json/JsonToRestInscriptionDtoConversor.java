package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestInscriptionDto;

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

}
