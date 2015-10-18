package testcode.xpath;

import org.apache.xpath.XPath;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;

public class XPathApacheXPathApi {

    public static void main(String[] args) throws Exception {
        Document doc = XmlUtils.loadDoc("/testcode/xpath/data.xml");

        String input = args.length != 0 ? args[1] : "guess' or '1'='1";
        String query = "//groups/group[@id='" + input + "']/writeAccess/text()";

        //selectNodeIterator
        NodeIterator iterator = XPathAPI.selectNodeIterator(doc, query);
        XmlUtils.printNodeIterator(iterator);

        //selectNodeList
        NodeList nodeList = XPathAPI.selectNodeList(doc, query);
        XmlUtils.printNodeList(nodeList);

        //selectSingleNode
        Node node = XPathAPI.selectSingleNode(doc, query);
        XmlUtils.printNode(node);

        //Static string (safe)
        Node node2 = XPathAPI.selectSingleNode(doc, "//groups/group[@id='guess']/writeAccess/text()".toLowerCase());
        XmlUtils.printNode(node2);
    }
}
