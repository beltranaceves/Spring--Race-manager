package es.udc.ws.races.service.exceptions;

import java.time.LocalDateTime;

@SuppressWarnings("serial")
public class InscriptionDateOverException extends Exception {

    private Long raceId;
    private LocalDateTime dateOver;

    public InscriptionDateOverException(Long raceId, LocalDateTime dateOver) {
        super("Cannot inscribe to race with id = " + raceId +
                " because the inscription date has expired (dateOver = " +
                dateOver + ")");
        this.raceId = raceId;
        this.dateOver = dateOver;
    }

    public Long getRaceId() {
        return raceId;
    }

    public LocalDateTime getDateOver() {
        return dateOver;
    }

    public void setDateOver(LocalDateTime dateOver) {
        this.dateOver = dateOver;
    }

    public void setRaceId(Long raceId) {
        this.raceId = raceId;
    }
}
