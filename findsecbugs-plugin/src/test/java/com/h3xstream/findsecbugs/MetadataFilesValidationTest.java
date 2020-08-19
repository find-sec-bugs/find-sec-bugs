/**
 * Find Security Bugs
 * Copyright (c) Philippe Arteau, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package com.h3xstream.findsecbugs;

import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.fail;


public class MetadataFilesValidationTest {

    /**
     * If this test fail, a new description in messages.xml was likely added by copy-pasting a previous entry.
     * See the failure message to see the duplicate description.
     * @throws Exception
     */
    @Test
    public void findDuplicateInMessagesXml() throws Exception {
        List<String> bugPatterns = new ArrayList<>();
        List<String> bugCodes = new ArrayList<>();

        List<String> shortDescriptions = new ArrayList<>();
        List<String> longDescriptions = new ArrayList<>();
        List<String> bugCodeDescriptions = new ArrayList<>();

        iterateDocNode("/metadata/messages.xml", (n) -> {

            //BugPattern node checks
            if(n.getNodeName().equals("BugPattern")) {

                if(n instanceof Element) {
                    Element dNode = (Element) n;

                    String type = dNode.getAttribute("type");
//                    System.out.println(type);
                    if(bugPatterns.contains(type)) {
                        fail("Duplicate BugPattern name : "+type);
                    }
                    bugPatterns.add(type);
                }
            }


            if(n.getNodeName().equals("ShortDescription")) {
                String content = n.getTextContent().trim();
//                System.out.println(content);
                if(shortDescriptions.contains(content)) {
                    fail("Duplicate ShortDescription : "+content);
                }
                shortDescriptions.add(content);
            }

            if(n.getNodeName().equals("LongDescription")) {
                String content = n.getTextContent().trim();
//                System.out.println(content);
                if(longDescriptions.contains(content)) {
                    fail("Duplicate LongDescription : "+content);
                }
                longDescriptions.add(content);
            }


            if(n.getNodeName().equals("BugCode")) {
                String content = n.getTextContent().trim();
//                System.out.println(content);
                if(bugCodeDescriptions.contains(content)) {
                    fail("Duplicate BugCode description : "+content);
                }
                bugCodeDescriptions.add(content);
            }

            //BugCode
            if(n.getNodeName().equals("BugCode")) {

                if(n instanceof Element) {
                    Element dNode = (Element) n;

                    String abbrev = dNode.getAttribute("abbrev");
//                    System.out.println(abbrev);
                    if(bugCodes.contains(abbrev)) {
                        fail("Duplicate BugCode name : "+abbrev);
                    }
                    bugCodes.add(abbrev);
                }
            }
        });

        //Minimum check to test that the XML was actually read.
        assertNotEquals(bugPatterns.size(),0);
        assertNotEquals(bugCodes.size(),0);
        assertNotEquals(shortDescriptions.size(),0);
        assertNotEquals(longDescriptions.size(),0);
        assertNotEquals(bugCodeDescriptions.size(),0);

    }

    private void iterateDocNode(String resource, NodeListener nodeListener) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder db = dbf.newDocumentBuilder();


        Document doc = db.parse(getClass().getResourceAsStream(resource));

        //Iterate on all XML nodes
        recurIterate(doc, nodeListener);
    }

    private void recurIterate(Node currentNode, NodeListener nodeListener) {
        NodeList nodeList = currentNode.getChildNodes();
        for(int i=0 ; i<nodeList.getLength() ;i++ ) {
            Node child = nodeList.item(i);
            nodeListener.newNode(child);
            recurIterate(child, nodeListener);
        }
    }

    private interface NodeListener {
        void newNode(Node node);
    }
}
