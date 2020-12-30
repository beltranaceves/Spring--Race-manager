package es.udc.ws.app.model.raceservice.exceptions;

@SuppressWarnings("serial")
public class creditCardDoesNotMatchException extends Exception{

    //private String creditCard1, creditCard2;

    public creditCardDoesNotMatchException(String creditCard1, String creditCard2) {
        super("Credit card number: " + creditCard1 + " does not match the inscription one: "
        + creditCard2);
    }
}
