package testcode.xpath;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;

public class XPathJavax {

    public static void main(String[] args) throws Exception {

        Document doc = XmlUtils.loadDoc("/testcode/xpath/data.xml");

        String input = args.length != 0 ? args[1] : "guess' or '1'='1";

        String query = "//groups/group[@id='" + input + "']/writeAccess/text()";

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

        //Safe (The next sample should not be mark)
        System.out.println(">> Safe");
        {
            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xpath.compile("//groups/group[@id='admin']/writeAccess/text()");
            XmlUtils.printNodeList(evaluateXPath(doc, expr));
        }
    }

    public static NodeList evaluateXPath(Document doc, XPathExpression xpath) throws XPathExpressionException {
        return (NodeList) xpath.evaluate(doc, XPathConstants.NODESET);
    }


}
