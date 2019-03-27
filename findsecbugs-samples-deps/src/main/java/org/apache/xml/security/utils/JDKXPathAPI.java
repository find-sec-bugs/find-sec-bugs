package org.apache.xml.security.utils;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JDKXPathAPI implements XPathAPI {
    @Override
    public boolean evaluate(Node contextNode, Node xpathnode, String str, Node namespaceNode) {
        return false;
    }

    @Override
    public NodeList selectNodeList(Node contextNode, Node xpathnode, String str, Node namespaceNode) {
        return null;
    }
}
