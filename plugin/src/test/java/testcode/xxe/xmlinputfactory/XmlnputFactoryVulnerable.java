package testcode.xxe.xmlinputfactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.InputStream;

public class XmlnputFactoryVulnerable {


    public static void main(String[] args) throws Exception {
        new XmlnputFactoryVulnerable().loadXml();
    }

    public void loadXml() throws XMLStreamException {
        InputStream in = getClass().getResourceAsStream("/testcode/xxe/simple_xxe.xml");

        if(in == null) System.out.println("Oups file not found.");

        //parseXMLdefaultValue(in);
        parseXMLwithWrongFlag(in);
    }

    public void parseXMLdefaultValue(InputStream input) throws XMLStreamException {
        StringBuilder content = new StringBuilder();
        XMLInputFactory factory = XMLInputFactory.newFactory();
//        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
//        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        XMLStreamReader reader = factory.createXMLStreamReader(input);
        while(reader.hasNext()) {
            reader.next();
        }
    }

    public void parseXMLwithWrongFlag(InputStream input) throws XMLStreamException {
        StringBuilder content = new StringBuilder();
        XMLInputFactory factory = XMLInputFactory.newFactory();
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, true);
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, true);
        XMLStreamReader reader = factory.createXMLStreamReader(input);
        while(reader.hasNext()) {
            reader.next();
        }
    }
}
