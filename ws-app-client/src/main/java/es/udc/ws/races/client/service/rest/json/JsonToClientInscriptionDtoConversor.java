package es.udc.ws.races.client.service.rest.json;

import java.io.InputStream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.races.client.service.dto.ClientInscriptionDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonToClientInscriptionDtoConversor {

    public static ClientInscriptionDto toClientInscriptionDto(InputStream jsonSale) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonSale);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode inscriptionObject = (ObjectNode) rootNode;

                JsonNode saleIdNode = inscriptionObject.get("inscriptionId");
                Long saleId = (saleIdNode != null) ? saleIdNode.longValue() : null;

                Long raceId = inscriptionObject.get("raceId").longValue();
                int dorsalNumber = inscriptionObject.get("dorsalNumber").intValue();
                Boolean collected = inscriptionObject.get("collected").booleanValue();

                return new ClientInscriptionDto(saleId, raceId, dorsalNumber, collected);

            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

}

