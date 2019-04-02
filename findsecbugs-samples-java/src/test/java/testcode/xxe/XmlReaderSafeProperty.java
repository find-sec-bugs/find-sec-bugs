package testcode.xxe;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import testcode.xxe.util.PrintHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class XmlReaderSafeProperty {

    public static void receiveXMLStreamSecureProcessing(final InputStream inStream)
            throws ParserConfigurationException, SAXException, IOException {

        XMLReader reader = XMLReaderFactory.createXMLReader();
        // Secure processing enabled
        reader.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING,true);
        reader.setContentHandler(new PrintHandler());
        reader.parse(new InputSource(inStream));
    }

    public static void receiveXMLStreamDoctypeDisabled(final InputStream inStream)
            throws ParserConfigurationException, SAXException, IOException {

        XMLReader reader = XMLReaderFactory.createXMLReader();
        // Secure processing enabled
        reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
        reader.setContentHandler(new PrintHandler());
        reader.parse(new InputSource(inStream));
    }
}
