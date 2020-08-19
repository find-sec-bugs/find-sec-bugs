package testcode.saml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Arrays;

public class BufferHandler extends DefaultHandler {

    private String currentNodeName = null;
    private StringBuilder buffer = new StringBuilder();

    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        buffer = new StringBuilder();
        //System.out.println("Node = " + qName);
        currentNodeName = qName;
    }

    public void characters(char ch[], int start, int length)
            throws SAXException {

        //System.out.println("New content received");

        buffer.append(ch,start, length);

    }

    public void endElement (String uri, String localName, String qName)
            throws SAXException
    {
        System.out.println(qName + "=" + buffer.toString());
        buffer = new StringBuilder();
    }
}
