package es.udc.ws.app.model.raceservice.exceptions;

@SuppressWarnings("serial")
public class CreditCardDoesNotMatchException extends Exception{

    private String creditCard1, creditCard2;

    public CreditCardDoesNotMatchException(String creditCard1, String creditCard2) {
        super("Credit card number: " + creditCard1 + " does not match the inscription one: "
        + creditCard2);
        this.creditCard1 = creditCard1;
        this.creditCard2 = creditCard2;
    }

    public String getCreditCard1(){return creditCard1; }
    public String getCreditCard2(){return creditCard2; }
}
