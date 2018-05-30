package testcode.xxe.xmlinputfactory;

import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.FileNotFoundException;

public class FilteredReaderVulnerable {

    public static void parseFile(XMLEventReader reader) throws FileNotFoundException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader r = factory.createFilteredReader(reader, new EventFilter() {
            @Override
            public boolean accept(XMLEvent event) {
                return true;
            }
        });
    }
}
