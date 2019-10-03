package testcode.xpath;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class XmlUtils {

    public static Document loadDoc(String path) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = domFactory.newDocumentBuilder();

        InputStream in = XmlUtils.class.getResourceAsStream(path);
        return builder.parse(in);
    }

    public static void printNode(Node node) {
        System.out.println(node.getNodeValue());
    }

    public static void printNodeList(NodeList nodes) {
        for (int i = 0; i < nodes.getLength(); i++) {
            System.out.println(nodes.item(i).getNodeValue());
        }
    }

    public static void printNodeIterator(NodeIterator iterator) {
        Node n;
        while ((n = iterator.nextNode()) != null) {
            System.out.println("Node:" + n.getNodeValue());
        }
    }
}
