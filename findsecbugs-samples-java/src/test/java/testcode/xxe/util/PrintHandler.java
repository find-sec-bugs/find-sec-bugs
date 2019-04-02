package testcode.xxe.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PrintHandler extends DefaultHandler {
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        System.out.println("Node = " + qName);
    }

    public void characters(char ch[], int start, int length)
            throws SAXException {

        System.out.println("New content received");
        System.out.println(new String(ch).substring(start, start + length));
    }
}
