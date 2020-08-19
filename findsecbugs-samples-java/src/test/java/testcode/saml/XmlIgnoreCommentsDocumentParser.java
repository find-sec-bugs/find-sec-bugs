package testcode.saml;


import java.io.*;
import java.util.Arrays;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public class XmlIgnoreCommentsDocumentParser {
    
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerException {


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringComments(false); //True of false does not seems to do any difference.

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(XmlIgnoreCommentsDocumentParser.class.getResourceAsStream("/testcode/xml/simple.xml"));
        read(doc);
    }

    public static void read(Document doc) throws TransformerException {

        TransformerFactory factory1 = TransformerFactory.newInstance();
        Transformer transformer = factory1.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);
        String s = writer.toString();

        for(Integer line : Arrays.asList(0,1,2)) {
            System.out.println("== Node "+line+" ==");
            System.out.print("Text content :");
            System.out.println(doc.getElementsByTagName("email").item(line).getTextContent());
            System.out.print("ChildNodes :");
            System.out.println(doc.getElementsByTagName("email").item(line).getChildNodes());
            System.out.print("NodeValue :");
            System.out.println(doc.getElementsByTagName("email").item(line).getNodeValue());
            System.out.print("toString :");
            System.out.println(doc.getElementsByTagName("email").item(line).toString());

        }


        System.out.println("Original XML:");
        System.out.println(s);
    }
}
