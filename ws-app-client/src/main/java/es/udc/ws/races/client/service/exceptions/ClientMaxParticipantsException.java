package es.udc.ws.races.client.service.exceptions;

public class ClientMaxParticipantsException extends Exception {

    private Long raceId;

    public ClientMaxParticipantsException(Long raceId) {
        super("Cannot inscribe to race with id = " + raceId +
                " because all inscriptions are full.");
        this.raceId = raceId;
    }

    public Long getRaceId() {
        return raceId;
    }

    public void setRaceId(Long raceId) {
        this.raceId = raceId;
    }

}
