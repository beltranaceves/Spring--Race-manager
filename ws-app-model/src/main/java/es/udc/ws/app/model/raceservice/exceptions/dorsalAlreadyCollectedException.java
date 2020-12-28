package es.udc.ws.app.model.raceservice.exceptions;

@SuppressWarnings("serial")
public class dorsalAlreadyCollectedException extends Exception {

    public dorsalAlreadyCollectedException() {
        super("Dorsal already collected");
    }

}
