package es.udc.ws.app.model.raceservice.exceptions;

@SuppressWarnings("serial")
public class MaxParticipantsException extends Exception {

    private Long raceId;
    private int maxParticipants;

    public MaxParticipantsException(Long raceId, int maxParticipants) {
        super("Cannot inscribe to race with id = " + raceId +
                " because all inscriptions are full." +
                " (maxParticipants = " + maxParticipants + ")");
        this.raceId = raceId;
        this.maxParticipants = maxParticipants;
    }

    public Long getRaceId() {
        return raceId;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setRaceId(Long raceId) {
        this.raceId = raceId;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }
}
