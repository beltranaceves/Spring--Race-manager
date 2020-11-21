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
                int numberOfInscribed) {
        this.city = city;
        this.raceDescription = raceDescription;
        this.inscriptionPrice = inscriptionPrice;
        this.maxParticipants = maxParticipants;
        this.numberOfInscribed = numberOfInscribed;
    }

    public Race(Long raceId, String city, String raceDescription, double inscriptionPrice, int maxParticipants,
                int numberOfInscribed) {
        this(city, raceDescription, inscriptionPrice, maxParticipants, numberOfInscribed);
        this.raceId = raceId;
    }

    public Race(Long raceId, String city, String raceDescription, double inscriptionPrice, int maxParticipants,
                int numberOfInscribed, LocalDateTime creationDate, LocalDateTime scheduleDate) {
        this(raceId, city, raceDescription, inscriptionPrice, maxParticipants, numberOfInscribed);
        this.creationDate = (creationDate != null) ? creationDate.withNano(0) : null;
        this.scheduleDate = (scheduleDate != null) ? scheduleDate.withNano(0) : null;
    }
}
