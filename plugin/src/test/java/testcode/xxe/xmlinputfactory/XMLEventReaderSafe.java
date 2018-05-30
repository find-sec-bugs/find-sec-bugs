package testcode.xxe.xmlinputfactory;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * https://docs.oracle.com/cd/E13222_01/wls/docs100/xml/stax.html
 */
public class XMLEventReaderSafe {

    public static void main(String[] args) throws Exception {
        parseFile(new InputStreamReader(XMLEventReaderSafe.class.getResourceAsStream("/testcode/xxe/simple_xxe.xml")));
    }
    
    public static void parseFile(Reader reader) throws FileNotFoundException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        //Ref: https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Prevention_Cheat_Sheet#XMLInputFactory_.28a_StAX_parser.29
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false); // This disables DTDs entirely for that factory
        factory.setProperty("javax.xml.stream.isSupportingExternalEntities", false); // disable external entities
        XMLEventReader r = factory.createXMLEventReader(reader);
        while(r.hasNext()) {
            XMLEvent e = r.nextEvent();
            System.out.println("ID:"+e.hashCode()+"["+e+"]");
        }
    }
}
