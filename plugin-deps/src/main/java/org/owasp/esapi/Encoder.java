package org.owasp.esapi;

public interface Encoder {
    String encodeForHTML(String input);
    String decodeForHTML(String input);
}
