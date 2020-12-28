package es.udc.ws.app.model.util;

import es.udc.ws.util.exceptions.InputValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class PropertyValidatorAditional {

    private PropertyValidatorAditional() {}

    public static void validateInt(String propertyName,
                                   int intValue, int lowerValidLimit, int upperValidLimit)
            throws InputValidationException {

        if ((intValue < lowerValidLimit) ||
                (intValue > upperValidLimit)) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be greater than " + lowerValidLimit +
                    " and lower than " + upperValidLimit + "): " +
                    intValue);
        }
    }

    public static void validateUserEmail(String userEmailValue) throws InputValidationException {

        /* Regular expression of email format permitted by RFC 5322. */
        String regex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userEmailValue);

        if ( (userEmailValue == null) || (!matcher.matches()) ) {
            throw new InputValidationException("Invalid user email value: "
                    + userEmailValue + ". It cannot be null and must "
                    + "match a valid format.");
        }
    }

}
