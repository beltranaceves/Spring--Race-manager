package es.udc.ws.app.client.service.exceptions;

public class ClientCreditCardDoesNotMatchException extends Exception{

    private String creditCard1, creditCard2;

    public ClientCreditCardDoesNotMatchException(String creditCard1, String creditCard2) {
        super("Credit card number: " + creditCard1 + " does not match the inscription one: "
                + creditCard2);
        this.creditCard1 = creditCard1;
        this.creditCard2 = creditCard2;
    }

    public String getCreditCard1(){return creditCard1; }
    public String getCreditCard2(){return creditCard2; }
}
