package es.udc.ws.races.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.races.restservice.dto.RestInscriptionDto;

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

}
