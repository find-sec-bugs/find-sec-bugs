package testcode.xxe.xpathexpression;


import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;

public class XPathExpressionSafe {


    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
        df.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
//        df.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
//        df.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        DocumentBuilder builder = df.newDocumentBuilder();

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression xPathExpr = xpath.compile("/xmlhell/text()");

        String result = xPathExpr.evaluate( builder.parse(getXmlStream()) );
    }

    public static void main2(String[] args) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
        df.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
//        df.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
//        df.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        DocumentBuilder builder = df.newDocumentBuilder();

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression xPathExpr = xpath.compile("/xmlhell/text()");

        String result = xPathExpr.evaluate( builder.parse(getXmlStream()) );
    }

    public static InputStream getXmlStream() {
        return XPathExpressionSafe.class.getResourceAsStream("/testcode/xxe/simple_xxe.xml");
    }
}
