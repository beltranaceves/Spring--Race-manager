package es.udc.ws.races.model.raceservice.exceptions;

@SuppressWarnings("serial")
public class AlreadyInscribedException extends Exception {

    private Long raceId;
    private String userEmail;

    public AlreadyInscribedException(Long raceId, String userEmail) {
        super("Cannot inscribe to race with id = " + raceId +
                " because the user with email " + userEmail + "is already inscribed.");
        this.raceId = raceId;
        this.userEmail = userEmail;
    }

    public Long getRaceId() {
        return raceId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setRaceId(Long raceId) {
        this.raceId = raceId;
    }
}
