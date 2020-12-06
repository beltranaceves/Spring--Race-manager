package es.udc.ws.races.service.exceptions;

@SuppressWarnings("serial")
public class MaxParticipantsException extends Exception {

    private Long raceId;

    public MaxParticipantsException(Long raceId) {
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
