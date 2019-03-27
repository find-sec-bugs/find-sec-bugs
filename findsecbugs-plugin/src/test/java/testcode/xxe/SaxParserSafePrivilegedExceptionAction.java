package testcode.xxe;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;

public class SaxParserSafePrivilegedExceptionAction {

    private static final AccessControlContext RESTRICTED_ACCESS_CONTROL;

    static {
        RESTRICTED_ACCESS_CONTROL = new AccessControlContext(new ProtectionDomain[]{
                new ProtectionDomain(null, null) // no permissions
        });
    }

    private static void receiveXMLStream(final InputStream inStream,
                                         final DefaultHandler defHandler)
            throws ParserConfigurationException, SAXException, IOException {
        // ...
        SAXParserFactory spf = SAXParserFactory.newInstance();
        final SAXParser saxParser = spf.newSAXParser();

        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction() {
                public Object run() throws SAXException, IOException {
                    saxParser.parse(inStream, defHandler);
                    return null;
                }
            }, RESTRICTED_ACCESS_CONTROL); // From nested class
        } catch (PrivilegedActionException pae) {
            System.out.println("Filesystem access blocked");
            pae.printStackTrace();
        }

    }

    public static void main(String[] args) throws ParserConfigurationException,
            SAXException, IOException {

        String xmlString = "<?xml version=\"1.0\"?>" +
                "<!DOCTYPE foo SYSTEM \"C:/test111\"><test>&foo;</test>"; // Tainted input

        InputStream is = new ByteArrayInputStream(xmlString.getBytes());
        receiveXMLStream(is, new DefaultHandler());
    }
}
