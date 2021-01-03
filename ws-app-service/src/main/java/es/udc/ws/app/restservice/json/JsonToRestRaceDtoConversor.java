package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestRaceDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
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

    public static ObjectNode toObjectNodeComplete(RestRaceDto race) {

        ObjectNode raceNode = JsonNodeFactory.instance.objectNode();

        if (race.getRaceId() != null) {
            raceNode.put("raceId", race.getRaceId());
        }
        raceNode.put("maxParticipants", race.getMaxParticipants()).
                put("numberOfInscribed", race.getNumberOfInscribed()).
                put("city", race.getCity()).
                put("description", race.getDescription()).
                put("date", race.getDate().toString()).
                put("inscriptionPrice", race.getInscriptionPrice());

        return raceNode;
    }

    public static ArrayNode toArrayNodeComplete(List<RestRaceDto> races) {

        ArrayNode racesNode = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < races.size(); i++) {
            RestRaceDto raceDto = races.get(i);
            ObjectNode raceObject = toObjectNodeComplete(raceDto);
            racesNode.add(raceObject);
        }

        return racesNode;
    }

    public static RestRaceDto toServiceRaceDto(InputStream jsonRace) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonRace);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode raceObject = (ObjectNode) rootNode;

                JsonNode raceIdNode = raceObject.get("raceId");
                Long raceId = (raceIdNode != null) ? raceIdNode.longValue() : null;

                String city = raceObject.get("city").textValue().trim();
                String description = raceObject.get("description").textValue().trim();
                LocalDateTime date =  LocalDateTime.parse(raceObject.get("date").asText());
                Double inscriptionPrice = raceObject.get("inscriptionPrice").asDouble();
                int maxParticipants = raceObject.get("maxParticipants").asInt();
                RestRaceDto restRaceDto = new RestRaceDto(raceId, maxParticipants, 0);
                restRaceDto.setCity(city);
                restRaceDto.setDescription(description);
                restRaceDto.setDate(date);
                restRaceDto.setInscriptionPrice(inscriptionPrice);

                return restRaceDto;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

}