package testcode.xxe.xmlinputfactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;

public class XmlInputFactorySafe {
    public static void main(String[] args) throws Exception {
        new XmlInputFactorySafe().loadXml();
    }

    public void loadXml() throws XMLStreamException {
        InputStream in = getClass().getResourceAsStream("/testcode/xxe/simple_xxe.xml");

        if(in == null) System.out.println("Oups file not found.");

//        parseXMLSafe1(in);
        parseXMLSafe2(in);
//        parseXMLSafe3(in);
    }

    public void parseXMLSafe1(InputStream input) throws XMLStreamException {

        XMLInputFactory factory = XMLInputFactory.newFactory();
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        XMLStreamReader reader = factory.createXMLStreamReader(input);
        while(reader.hasNext()) {
            reader.next();
        }
    }

    public void parseXMLSafe2(InputStream input) throws XMLStreamException {

        XMLInputFactory factory = XMLInputFactory.newFactory();
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        XMLStreamReader reader = factory.createXMLStreamReader(input);
        while(reader.hasNext()) {
            reader.next();
        }
    }

    public void parseXMLSafe3(InputStream input) throws XMLStreamException {

        XMLInputFactory factory = XMLInputFactory.newFactory();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        XMLStreamReader reader = factory.createXMLStreamReader(input);
        while(reader.hasNext()) {
            reader.next();
        }
    }

    public void parseXMLSafe4(InputStream input) throws XMLStreamException {

        XMLInputFactory factory = XMLInputFactory.newFactory();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        XMLStreamReader reader = factory.createXMLStreamReader(input);
        while(reader.hasNext()) {
            reader.next();
        }
    }
}
