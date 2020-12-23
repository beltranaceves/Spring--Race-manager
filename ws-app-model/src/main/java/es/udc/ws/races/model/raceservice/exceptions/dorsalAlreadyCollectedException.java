package es.udc.ws.races.model.raceservice.exceptions;

@SuppressWarnings("serial")
public class dorsalAlreadyCollectedException extends Exception {

    public dorsalAlreadyCollectedException() {
        super("Dorsal already collected");
    }

}
