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
package com.h3xstream.findsecbugs.graph;

import com.h3xstream.findbugs.test.EasyBugReporter;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.testng.annotations.Test;

import java.util.Map;

import static com.h3xstream.findsecbugs.graph.util.GraphQueryUtil.iterable;
import static com.h3xstream.findsecbugs.graph.util.GraphQueryUtil.printNode;
import static org.mockito.Mockito.spy;
import static org.testng.Assert.assertTrue;

public class CWE113_HTTP_Response_Splitting__getParameter_Servlet_addHeaderServlet_45Test extends BaseGraphDetectorTest {

    @Test
    public void analyzeFieldToSink() throws Exception {
        //GraphInstance.mustDeleteFileCreated = false; //For troubleshooting only

        //Locate test code
        String[] files = {
                getClassFilePath("testcases/CWE113_HTTP_Response_Splitting/s02/CWE113_HTTP_Response_Splitting__getParameter_Servlet_addHeaderServlet_45")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        try(Transaction tx = db.beginTx()) {

            //Check specific file handle
            {
                Result res = db.execute("MATCH (source:Variable)-[t:TRANSFER*0..8]->(n:Variable{name:\"javax/servlet/http/HttpServletResponse.addHeader(Ljava/lang/String;Ljava/lang/String;)V_p0\"})\n" +
                        "WHERE source.state = \"TAINTED\"\n" +
                        "RETURN source,t,n", HashMapBuilder.buildObj());

                assertTrue(iterable(res).iterator().hasNext());

                for (Map<String, Object> node : iterable(res)) {
                    Node n = (Node) node.get("n");
                    System.out.println(printNode(n));
                }
            }

            tx.success();
        }

    }
}
