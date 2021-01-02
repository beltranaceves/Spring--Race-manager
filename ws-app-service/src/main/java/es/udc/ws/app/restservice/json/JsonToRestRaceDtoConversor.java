package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestRaceDto;

import java.util.List;

public class JsonToRestRaceDtoConversor {

    public static ObjectNode toObjectNode(RestRaceDto race) {

        ObjectNode raceNode = JsonNodeFactory.instance.objectNode();

        if (race.getRaceId() != null) {
            raceNode.put("raceId", race.getRaceId());
        }
        raceNode.put("maxParticipants", race.getMaxParticipants()).
                put("numberOfInscribed", race.getNumberOfInscribed());

        return raceNode;
    }

    public static ArrayNode toArrayNode(List<RestRaceDto> races) {

        ArrayNode racesNode = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < races.size(); i++) {
            RestRaceDto raceDto = races.get(i);
            ObjectNode raceObject = toObjectNode(raceDto);
            racesNode.add(raceObject);
        }

        return racesNode;
    }

}
