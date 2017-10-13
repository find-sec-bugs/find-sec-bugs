package testcode.xxe.transformerfactory;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.StringWriter;

public class TransformerFactorySafe {
    public static void main(String[] args) throws Exception {
        new TransformerFactorySafe().loadXml();
    }

    public void loadXml() throws XMLStreamException, TransformerException {
        InputStream in = getClass().getResourceAsStream("/testcode/xxe/simple_xxe.xml");
        InputStream xslt_in = getClass().getResourceAsStream("/testcode/xxe/simple_xxe.xslt");

        if(in == null) System.out.println("Oups XML file not found.");
        if(xslt_in == null) System.out.println("Oups XSLT file not found.");

        Source source = new StreamSource(in);
        Source xslt = new StreamSource(xslt_in);

        //parseXMLSafe1(source);
        //parseXMLSafe2(source);
        //parseXSLTSafe1(source, xslt);
        //parseXSLTSafe2(source, xslt);
    }

    // https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Prevention_Cheat_Sheet#TransformerFactory
    public void parseXMLSafe1(Source input) throws XMLStreamException, TransformerException {

        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StringWriter outWriter = new StringWriter();
        StreamResult result = new StreamResult(outWriter);

        transformer.transform(input, result);
        outWriter.toString();
    }

    public void parseXMLSafe2(Source input) throws XMLStreamException, TransformerException {

        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StringWriter outWriter = new StringWriter();
        StreamResult result = new StreamResult(outWriter);

        transformer.transform(input, result);
        outWriter.toString();
    }

    // https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Prevention_Cheat_Sheet#TransformerFactory
    public void parseXSLTSafe1(Source input, Source xslt) throws XMLStreamException, TransformerException {

        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

        Transformer transformer = factory.newTransformer(xslt);
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StringWriter outWriter = new StringWriter();
        StreamResult result = new StreamResult(outWriter);

        transformer.transform(input, result);
        outWriter.toString();
    }

    public void parseXSLTSafe2(Source input, Source xslt) throws XMLStreamException, TransformerException {

        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

        Transformer transformer = factory.newTransformer(xslt);
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StringWriter outWriter = new StringWriter();
        StreamResult result = new StreamResult(outWriter);

        transformer.transform(input, result);
        outWriter.toString();
    }
}
