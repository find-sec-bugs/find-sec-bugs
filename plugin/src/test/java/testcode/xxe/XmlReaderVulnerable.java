package testcode.xxe;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import testcode.xxe.util.PrintHandler;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class XmlReaderVulnerable {
    private static void receiveXMLStream(final InputStream inStream,
                                         final DefaultHandler defHandler)
            throws ParserConfigurationException, SAXException, IOException {
        // ...
        XMLReader reader = XMLReaderFactory.createXMLReader();
        reader.setContentHandler(new PrintHandler());
        reader.parse(new InputSource(inStream));
    }


    public static void main(String[] args) throws ParserConfigurationException,
            SAXException, IOException {

        String xmlString = "<?xml version=\"1.0\"?>" +
                "<!DOCTYPE test [ <!ENTITY foo SYSTEM \"C:/Code/public.txt\"> ]><test>&foo;</test>"; // Tainted input

        InputStream is = new ByteArrayInputStream(xmlString.getBytes());
        receiveXMLStream(is, new DefaultHandler());
    }
}
