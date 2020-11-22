package es.udc.ws.races.model.util.exceptions;

import java.time.LocalDateTime;

@SuppressWarnings("serial")
public class dorsalAlreadyCollectedException extends Exception {

    public dorsalAlreadyCollectedException() {
        super("Dorsal already collected");
    }

}
