package org.apache.xml.security.utils;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;

public interface XPathAPI {
    boolean	evaluate(Node contextNode, Node xpathnode, String str, Node namespaceNode);
    NodeList selectNodeList(Node contextNode, Node xpathnode, String str, Node namespaceNode);
}
