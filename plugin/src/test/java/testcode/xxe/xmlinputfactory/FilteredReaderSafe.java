package testcode.xxe.xmlinputfactory;

import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;

public class FilteredReaderSafe {

    public static void parseFile(XMLEventReader reader) throws FileNotFoundException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        //Ref: https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Prevention_Cheat_Sheet#XMLInputFactory_.28a_StAX_parser.29
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false); // This disables DTDs entirely for that factory
        factory.setProperty("javax.xml.stream.isSupportingExternalEntities", false); // disable external entities
        XMLEventReader r = factory.createFilteredReader(reader, new EventFilter() {
            @Override
            public boolean accept(XMLEvent event) {
                return true;
            }
        });
    }
}
