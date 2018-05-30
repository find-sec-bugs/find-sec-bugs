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
public class XMLEventReaderVulnerable {

    public static void main(String[] args) throws Exception {
        parseFile(new InputStreamReader(XMLEventReaderVulnerable.class.getResourceAsStream("/testcode/xxe/simple_xxe.xml")));
    }

    public static void parseFile(Reader reader) throws FileNotFoundException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader r = factory.createXMLEventReader(reader);
        while(r.hasNext()) {
            XMLEvent e = r.nextEvent();
            System.out.println("ID:"+e.hashCode()+"["+e+"]");
        }
    }
}
