package testcode.xxe;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SaxParserVulnerable {

    private static void receiveXMLStream(final InputStream inStream,
                                         final DefaultHandler defHandler)
            throws ParserConfigurationException, SAXException, IOException {
        // ...
        SAXParserFactory spf = SAXParserFactory.newInstance();
        final SAXParser saxParser = spf.newSAXParser();
        saxParser.parse(inStream, defHandler);
    }

    public static void main(String[] args) throws ParserConfigurationException,
            SAXException, IOException {

        String xmlString = "<?xml version=\"1.0\"?>" +
                "<!DOCTYPE test [  <!ENTITY foo SYSTEM \"C:/Code/public.txt\"> ]><test>&foo;</test>"; // Tainted input

        InputStream is = new ByteArrayInputStream(xmlString.getBytes());
        receiveXMLStream(is, new DefaultHandler());
    }
}
