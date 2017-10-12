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

public class TransformerFactoryVulnerable {


    public static void main(String[] args) throws Exception {
        new TransformerFactoryVulnerable().loadXml();
    }

    public void loadXml() throws XMLStreamException, TransformerException {
        InputStream in = getClass().getResourceAsStream("/testcode/xxe/simple_xxe.xml");
        InputStream dos_in = getClass().getResourceAsStream("/testcode/xxe/dos_xxe.xml");
        InputStream xslt_in = getClass().getResourceAsStream("/testcode/xxe/simple_xxe.xslt");

        if(in == null) System.out.println("Oups XML file not found.");
        if(dos_in == null) System.out.println("Oups XML DoS file not found.");
        if(xslt_in == null) System.out.println("Oups XSLT file not found.");

        Source source = new StreamSource(in);
        Source source_dos = new StreamSource(dos_in);
        Source xslt = new StreamSource(xslt_in);

        parseXMLdefaultValue(source);
        //parseXMLDoS(source_dos);
        //parseXMLOneLiner(source);
        //parseXMLWithXslt(source, xslt);
        //parseXMLWithMissingAttributeStylesheet(source, xslt);
        //parseXMLWithMissingAttributeDtd(source, xslt);
        //parseXMLWithWrongFlag1(source);
        //parseXMLWithWrongFlag2(source);
    }

    public void parseXMLdefaultValue(Source input) throws XMLStreamException, TransformerException {

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();

        StringWriter outWriter = new StringWriter();
        StreamResult result = new StreamResult(outWriter);

        transformer.transform(input, result);
        outWriter.toString();
    }

    public void parseXMLDoS(Source input) throws XMLStreamException, TransformerException {

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();

        StringWriter outWriter = new StringWriter();
        StreamResult result = new StreamResult(outWriter);

        transformer.transform(input, result);
        outWriter.toString();
    }

    public void parseXMLOneLiner(Source input) throws XMLStreamException, TransformerException {

        Transformer transformer = TransformerFactory.newInstance().newTransformer();

        StringWriter outWriter = new StringWriter();
        StreamResult result = new StreamResult(outWriter);

        transformer.transform(input, result);
        outWriter.toString();
    }

    public void parseXMLWithXslt(Source input, Source xslt) throws XMLStreamException, TransformerException {

        Transformer transformer = TransformerFactory.newInstance().newTransformer(xslt);

        StringWriter outWriter = new StringWriter();
        StreamResult result = new StreamResult(outWriter);

        transformer.transform(input, result);
        outWriter.toString();
    }

    public void parseXMLWithMissingAttributeStylesheet(Source input, Source xslt) throws XMLStreamException, TransformerException {

        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");

        Transformer transformer = factory.newTransformer(xslt);
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StringWriter outWriter = new StringWriter();
        StreamResult result = new StreamResult(outWriter);

        transformer.transform(input, result);
        result.toString();
    }

    public void parseXMLWithMissingAttributeDtd(Source input, Source xslt) throws XMLStreamException, TransformerException {

        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

        Transformer transformer = factory.newTransformer(xslt);
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StringWriter outWriter = new StringWriter();
        StreamResult result = new StreamResult(outWriter);

        transformer.transform(input, result);
        result.toString();
    }

    public void parseXMLWithWrongFlag1(Source input) throws XMLStreamException, TransformerException {

        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "all");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "all");

        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StringWriter outWriter = new StringWriter();
        StreamResult result = new StreamResult(outWriter);

        transformer.transform(input, result);
        result.toString();
    }

    public void parseXMLWithWrongFlag2(Source input) throws XMLStreamException, TransformerException {

        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);

        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StringWriter outWriter = new StringWriter();
        StreamResult result = new StreamResult(outWriter);

        transformer.transform(input, result);
        result.toString();
    }
}
