package es.udc.ws.app.client.service.exceptions;

import java.time.LocalDateTime;

public class ClientInscriptionDateOverException extends Exception {

    private Long raceId;
    private LocalDateTime dateOver;

    public ClientInscriptionDateOverException(Long raceId, LocalDateTime dateOver) {
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
