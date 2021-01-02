package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.race.Race;

import java.util.ArrayList;
import java.util.List;

public class RaceToRestRaceDtoConversor {
    public static RestRaceDto toRestRaceDto(Race race) {
        return new RestRaceDto(race.getRaceId(), race.getMaxParticipants(),
                race.getNumberOfInscribed());
    }

    public static List<RestRaceDto> toRestRaceDtos(List<Race> races) {
        List<RestRaceDto> raceDtos = new ArrayList<>(races.size());
        for (int i = 0; i < races.size(); i++) {
            Race race = races.get(i);
            raceDtos.add(toRestRaceDto(race));
        }
        return raceDtos;
    }
}
