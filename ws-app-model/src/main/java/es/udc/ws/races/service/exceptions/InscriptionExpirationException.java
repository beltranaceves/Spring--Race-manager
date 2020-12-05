package es.udc.ws.races.service.exceptions;

import java.time.LocalDateTime;

@SuppressWarnings("serial")
public class InscriptionExpirationException extends Exception{

    private int dorsalNumber;
    private LocalDateTime dateOver;

    public InscriptionExpirationException(int dorsalNumber, LocalDateTime dateOver) {
        super("Cannot collect dorsal number=\"" + dorsalNumber +
                "\" because the race has already started (dateOver = \"" +
                dateOver + "\")");
        this.dorsalNumber = dorsalNumber;
        this.dateOver = dateOver;
    }

    public int getDorsalNumber() {
        return dorsalNumber;
    }

    public LocalDateTime getDateOver() {
        return dateOver;
    }

    public void setDateOver(LocalDateTime dateOver) {
        this.dateOver = dateOver;
    }

    public void setDorsalNumber(int DorsalNumber) {
        this.dorsalNumber = dorsalNumber;
    }
}
