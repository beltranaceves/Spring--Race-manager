package es.udc.ws.app.model.raceservice.exceptions;

@SuppressWarnings("serial")
public class DorsalAlreadyCollectedException extends Exception {

    private Long inscriptionID;

    public DorsalAlreadyCollectedException(Long inscriptionID) {
        super("Dorsal already collected");
        this.inscriptionID = inscriptionID;
    }

    public Long getInscriptionID(){return inscriptionID; }

}
