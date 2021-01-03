package es.udc.ws.app.client.service.rest.json;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.app.client.service.dto.ClientRaceDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;


public class JsonToClientRaceDtoConversor {

    public static ClientRaceDto toClientRaceDto (InputStream jsonRace) throws ParsingException {

        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonRace);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode raceObject = (ObjectNode) rootNode;

                JsonNode raceIdNode = raceObject.get("raceId");
                Long raceId = (raceIdNode != null) ? raceIdNode.longValue() : null;

                int maxParticipants = raceObject.get("maxParticipants").intValue();
                int numberOfInscribed = raceObject.get("numberOfInscribed").intValue();
                
                return new ClientRaceDto(raceId, maxParticipants, numberOfInscribed);

            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientRaceDto> toClientRaceDtos(InputStream jsonRaces) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonRaces);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode racesArray = (ArrayNode) rootNode;
                List<ClientRaceDto> raceDtos = new ArrayList<>(racesArray.size());
                for (JsonNode raceNode : racesArray) {
                    raceDtos.add(toClientRaceDto(raceNode));
                }

                return raceDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static ClientRaceDto toClientRaceDtoComplete (InputStream jsonRace) throws ParsingException {

        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonRace);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode raceObject = (ObjectNode) rootNode;

                JsonNode raceIdNode = raceObject.get("raceId");
                Long raceId = (raceIdNode != null) ? raceIdNode.longValue() : null;

                int maxParticipants = raceObject.get("maxParticipants").intValue();
                int numberOfInscribed = raceObject.get("numberOfInscribed").intValue();
                String city = raceObject.get("city").asText();
                String description = raceObject.get("description").asText();
                Double inscriptionPrice = raceObject.get("inscriptionPrice").asDouble();
                LocalDateTime date = LocalDateTime.parse(raceObject.get("date").asText());
                ClientRaceDto clientRaceDto = new ClientRaceDto(raceId, maxParticipants, numberOfInscribed);
                clientRaceDto.setCity(city);
                clientRaceDto.setDescription(description);
                clientRaceDto.setInscriptionPrice(inscriptionPrice);
                clientRaceDto.setDate(date);
                return clientRaceDto;

            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientRaceDto> toClientRaceDtosComplete(InputStream jsonRaces) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonRaces);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode racesArray = (ArrayNode) rootNode;
                List<ClientRaceDto> raceDtos = new ArrayList<>(racesArray.size());
                for (JsonNode raceNode : racesArray) {
                    raceDtos.add(toClientRaceDtoComplete(raceNode));
                }

                return raceDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientRaceDto toClientRaceDtoComplete(JsonNode raceNode) throws ParsingException {
        if (raceNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode raceObject = (ObjectNode) raceNode;

            JsonNode raceIdNode = raceObject.get("raceId");
            Long raceId = (raceIdNode != null) ? raceIdNode.longValue() : null;

            int maxParticipants = raceObject.get("maxParticipants").intValue();
            int numberOfInscribed = raceObject.get("numberOfInscribed").intValue();
            String city = raceObject.get("city").asText();
            String description = raceObject.get("description").asText();
            Double inscriptionPrice = raceObject.get("inscriptionPrice").asDouble();
            LocalDateTime date = LocalDateTime.parse(raceObject.get("date").asText());
            ClientRaceDto clientRaceDto = new ClientRaceDto(raceId, maxParticipants, numberOfInscribed);
            clientRaceDto.setCity(city);
            clientRaceDto.setDescription(description);
            clientRaceDto.setInscriptionPrice(inscriptionPrice);
            clientRaceDto.setDate(date);
            return clientRaceDto;
        }
    }

    private static ClientRaceDto toClientRaceDto(JsonNode raceNode) throws ParsingException {
        if (raceNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode raceObject = (ObjectNode) raceNode;

            JsonNode raceIdNode = raceObject.get("raceId");
            Long raceId = (raceIdNode != null) ? raceIdNode.longValue() : null;

            int maxParticipants = raceObject.get("maxParticipants").intValue();
            int numberOfInscribed = raceObject.get("numberOfInscribed").intValue();

            return new ClientRaceDto(raceId, maxParticipants, numberOfInscribed);
        }
    }
    
    public static ObjectNode toObjectNode(ClientRaceDto race) throws IOException {

		ObjectNode raceObject = JsonNodeFactory.instance.objectNode();

		if (race.getRaceId() != null) {
			raceObject.put("raceId", race.getRaceId());
		}
		raceObject.put("city", race.getCity()).
			put("description", race.getDescription()).
			put("date", race.getDate().toString()).
			put("inscriptionPrice", race.getInscriptionPrice()).
			put("maxParticipants", race.getMaxParticipants());
		
		return raceObject;
	}
}
