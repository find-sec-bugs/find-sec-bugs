package org.owasp.esapi;

public interface Encoder {
    String encodeForOS(org.owasp.esapi.codecs.Codec code,String input);

    String encodeForHTML(String input);
    String decodeForHTML(String input);
    String encodeForURL(String input);
    String decodeFromURL(String input);
    String encodeForXPath(String input);
}
