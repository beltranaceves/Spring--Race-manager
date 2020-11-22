package es.udc.ws.races.model.race;

import java.time.LocalDateTime;

public class Race {

    private Long raceId;
    private String city;
    private String raceDescription;
    private double inscriptionPrice;
    private int maxParticipants;
    private LocalDateTime creationDate;
    private LocalDateTime scheduleDate;
    private int numberOfInscribed;

    public Race(String city, String raceDescription, double inscriptionPrice, int maxParticipants,
                int numberOfInscribed, LocalDateTime scheduleDate) {
        this.city = city;
        this.raceDescription = raceDescription;
        this.inscriptionPrice = inscriptionPrice;
        this.scheduleDate = scheduleDate;
        this.maxParticipants = maxParticipants;
        this.numberOfInscribed = numberOfInscribed;
    }

    public Race(Long raceId, String city, String raceDescription, double inscriptionPrice, int maxParticipants,
                int numberOfInscribed, LocalDateTime scheduleDate) {
        this(city, raceDescription, inscriptionPrice,maxParticipants, numberOfInscribed, scheduleDate);
        this.raceId = raceId;
    }

    public Race(String city, String raceDescription, double inscriptionPrice, int maxParticipants,
                int numberOfInscribed, LocalDateTime creationDate, LocalDateTime scheduleDate) {
        this(city, raceDescription, inscriptionPrice, maxParticipants, numberOfInscribed, scheduleDate);
        this.creationDate = (creationDate != null) ? creationDate.withNano(0) : null;
    }

    public Race(Long raceId, String city, String raceDescription, double inscriptionPrice, int maxParticipants,
                int numberOfInscribed, LocalDateTime creationDate, LocalDateTime scheduleDate) {
        this(raceId, city, raceDescription, inscriptionPrice, maxParticipants, numberOfInscribed, scheduleDate);
        this.creationDate = (creationDate != null) ? creationDate.withNano(0) : null;
    }

    public Long getRaceId() {
        return raceId;
    }
    public void setRaceId(Long raceId) {
        this.raceId = raceId;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getRaceDescription() {
        return raceDescription;
    }
    public void setRaceDescription(String raceDescription) {
        this.raceDescription = raceDescription;
    }
    public double getInscriptionPrice() {
        return inscriptionPrice;
    }
    public void setInscriptionPrice(double inscriptionPrice) {
        this.inscriptionPrice = inscriptionPrice;
    }
    public int getMaxParticipants() {
        return maxParticipants;
    }
    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
    public LocalDateTime getScheduleDate() {
        return scheduleDate;
    }
    public void setScheduleDate(LocalDateTime scheduleDate) {
        this.scheduleDate = scheduleDate;
    }
    public int getNumberOfInscribed() {
        return numberOfInscribed;
    }
    public void setNumberOfInscribed(int numberOfInscribed) {
        this.numberOfInscribed = numberOfInscribed;
    }

}
