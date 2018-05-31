package testcode.xxe.transformerfactory;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class SaxTransformerFactoryVulnerable {


    public void parseXML(Source input) throws XMLStreamException, TransformerException {

        Transformer transformer = SAXTransformerFactory.newInstance().newTransformer();

        StringWriter outWriter = new StringWriter();
        StreamResult result = new StreamResult(outWriter);

        transformer.transform(input, result);
        outWriter.toString();
    }
}
