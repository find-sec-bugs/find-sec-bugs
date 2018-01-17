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

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Map;

import static com.h3xstream.findsecbugs.graph.util.GraphQueryUtil.iterable;
import static org.mockito.Mockito.spy;
import static org.testng.Assert.assertTrue;

public class CWE113_HTTP_Response_Splitting__Environment_addCookieServlet_45Test extends BaseDetectorTest {

    @Test
    public void analyzeFieldToSink() throws Exception {

        File tempDb = TempDatabase.createTempDirectory();
        GraphDatabaseService db = GraphInstance.getInstance().init(tempDb.getCanonicalPath());
        GraphInstance.mustDeleteDatabase = true;

        //Locate test code
        String[] files = {
                getClassFilePath("testcases/CWE113_HTTP_Response_Splitting/s01/CWE113_HTTP_Response_Splitting__Environment_addCookieServlet_45")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        try(Transaction tx = db.beginTx()) {

            //Check specific file handle
            {
                Result res = db.execute("MATCH (n:Variable{name:\"javax/servlet/http/Cookie.<init>(Ljava/lang/String;Ljava/lang/String;)V_p0\"})\n" +
                        "RETURN n;", HashMapBuilder.buildObj());

                assertTrue(iterable(res).iterator().hasNext());

                for (Map<String, Object> node : iterable(res)) {
                    Node n = (Node) node.get("n");
                    //System.out.println(printNode(n));
                }
            }

            tx.success();
        }

        GraphBuilder.clearCache();
    }


}
