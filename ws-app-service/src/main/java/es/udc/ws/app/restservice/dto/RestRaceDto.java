package es.udc.ws.app.restservice.dto;

import java.util.Objects;

public class RestRaceDto {

    private Long raceId;
    private int maxParticipants;
    private int numberOfInscribed;

    public RestRaceDto(Long raceId, int maxParticipants, int numberOfInscribed) {
        this.raceId = raceId;
        this.maxParticipants = maxParticipants;
        this.numberOfInscribed = numberOfInscribed;
    }

    public Long getRaceId() { return raceId; }
    public void setRaceId(Long raceId) { this.raceId = raceId; }

    public int getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(int maxParticipants) { this.maxParticipants = maxParticipants; }

    public int getNumberOfInscribed() { return numberOfInscribed; }
    public void setNumberOfInscribed(int numberOfInscribed) { this.numberOfInscribed = numberOfInscribed; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestRaceDto that = (RestRaceDto) o;
        return Objects.equals(raceId, that.raceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(raceId);
    }
}