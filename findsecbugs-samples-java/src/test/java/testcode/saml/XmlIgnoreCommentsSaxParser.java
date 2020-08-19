package testcode.saml;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import testcode.xxe.util.PrintHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class XmlIgnoreCommentsSaxParser {

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

        InputStream is = XmlIgnoreCommentsSaxParser.class.getResourceAsStream("/testcode/xml/simple.xml");
        receiveXMLStream(is, new BufferHandler());
    }
}
