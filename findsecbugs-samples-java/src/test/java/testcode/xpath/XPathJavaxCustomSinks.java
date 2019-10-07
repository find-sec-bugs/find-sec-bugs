package testcode.xpath;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class XPathJavaxCustomSinks {

    public static void main(String[] args) throws Exception {

        Document doc = XmlUtils.loadDoc("/testcode/xpath/data.xml");

        String input = args.length != 0 ? args[1] : "guess' or '1'='1";

        String query = "//groups/group[@id='" + input + "']/writeAccess/text()";

        XPathBadApi.query(query); //Custom sink that should be identify as vulnerable

    }

    public static NodeList evaluateXPath(Document doc, XPathExpression xpath) throws XPathExpressionException {
        return (NodeList) xpath.evaluate(doc, XPathConstants.NODESET);
    }


}
