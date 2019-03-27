package org.owasp.esapi;

import org.owasp.esapi.errors.IntrusionException;

public class Validator {
    public boolean isValidInput(String context, String input, String type, int maxLength, boolean allowNull) throws IntrusionException {
        return true;
    }
}
