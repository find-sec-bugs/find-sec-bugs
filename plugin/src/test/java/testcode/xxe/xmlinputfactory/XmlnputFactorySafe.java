package testcode.xxe.xmlinputfactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;

public class XmlnputFactorySafe {
    public static void main(String[] args) throws Exception {
        new XmlnputFactorySafe().loadXml();
    }

    public void loadXml() throws XMLStreamException {
        InputStream in = getClass().getResourceAsStream("/testcode/xxe/simple_xxe.xml");

        if(in == null) System.out.println("Oups file not found.");

        parseXMLSafe(in);
    }

    public void parseXMLSafe(InputStream input) throws XMLStreamException {

        XMLInputFactory factory = XMLInputFactory.newFactory();
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        XMLStreamReader reader = factory.createXMLStreamReader(input);
        while(reader.hasNext()) {
            reader.next();
        }
    }
}
