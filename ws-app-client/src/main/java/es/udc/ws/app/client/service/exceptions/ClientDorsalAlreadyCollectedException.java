package es.udc.ws.app.client.service.exceptions;

public class ClientDorsalAlreadyCollectedException extends Exception{

    private Long inscriptionID;

    public ClientDorsalAlreadyCollectedException(Long inscriptionID) {
        super("Dorsal already collected");
        this.inscriptionID = inscriptionID;
    }

    public Long getInscriptionID(){return inscriptionID; }

}
