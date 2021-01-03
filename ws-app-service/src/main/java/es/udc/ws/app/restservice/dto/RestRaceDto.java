package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class RestRaceDto {

    private Long raceId;
    private int maxParticipants;
    private int numberOfInscribed;

    private String city;
    private String description;
    private LocalDateTime date;
    private Double inscriptionPrice;

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

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public Double getInscriptionPrice() { return inscriptionPrice; }
    public void setInscriptionPrice(Double inscriptionPrice) { this.inscriptionPrice = inscriptionPrice; }

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