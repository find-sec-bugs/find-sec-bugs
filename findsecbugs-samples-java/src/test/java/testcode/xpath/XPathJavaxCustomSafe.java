package testcode.xpath;

import org.owasp.esapi.ESAPI;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class XPathJavaxCustomSafe {

    public static void main(String[] args) throws Exception {

        Document doc = XmlUtils.loadDoc("/testcode/xpath/data.xml");

        String input = args.length != 0 ? args[1] : "guess' or '1'='1";

        String query = "//groups/group[@id='" + XPathSuperSecureUtil.encodeForXPath(input) + "']/writeAccess/text()";

        System.out.println(">> XPath.compile()");
        {
            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xpath.compile(query);

            XmlUtils.printNodeList(evaluateXPath(doc, expr));
        }

        System.out.println(">> XPath.evaluate()");
        {
            XPath xpath = XPathFactory.newInstance().newXPath();
            String result = xpath.evaluate(query, doc);
            System.out.println("result=" + result);
        }
    }

    public static NodeList evaluateXPath(Document doc, XPathExpression xpath) throws XPathExpressionException {
        return (NodeList) xpath.evaluate(doc, XPathConstants.NODESET);
    }


}
