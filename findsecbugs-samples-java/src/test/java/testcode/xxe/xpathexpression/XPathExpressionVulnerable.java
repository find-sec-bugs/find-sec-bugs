package testcode.xxe.xpathexpression;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class XPathExpressionVulnerable {


    public static void main(String[] args) throws Exception {
        unsafe1();
        unsafe2();
        unsafe3();

    }
    public static void unsafe1() throws Exception {
        DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
        //df.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        //df.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        DocumentBuilder builder = df.newDocumentBuilder();

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression xPathExpr = xpath.compile("/xmlhell/text()");

        String result = xPathExpr.evaluate( builder.parse(getXmlStream()) );
    }

    public static void unsafe2() throws Exception {
        DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
        //df.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        //df.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        DocumentBuilder builder = df.newDocumentBuilder();

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression xPathExpr = xpath.compile("/xmlhell/text()");

        String result = xPathExpr.evaluate(new InputSource(getXmlStream()));
    }

    public static void unsafe3() throws Exception {
        DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
        //df.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        //df.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        DocumentBuilder builder = df.newDocumentBuilder();

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression xPathExpr = xpath.compile("/xmlhell/text()");

        xPathExpr.evaluate(new InputSource(new InputStreamReader(getXmlStream())), null);
    }

    public static InputStream getXmlStream() {
        return XPathExpressionVulnerable.class.getResourceAsStream("/testcode/xxe/simple_xxe.xml");
    }
}
