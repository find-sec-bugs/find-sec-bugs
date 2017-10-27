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
import org.neo4j.graphdb.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.h3xstream.findsecbugs.graph.util.GraphQueryUtil.iterable;
import static org.mockito.Mockito.spy;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 *
 */
public class GraphGadgetTest extends BaseDetectorTest {

    @Test
    public void analyzeGadget() throws Exception {
//        FindSecBugsGlobalConfig.getInstance().setDebugPrintInstructionVisited(true);
        //FindSecBugsGlobalConfig.getInstance().setDebugTaintState(true);


        File tempDb = TempDatabase.createTempDirectory();
        GraphDatabaseService db = GraphInstance.getInstance().init(tempDb.getCanonicalPath());

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/gadget/cachedata/CacheData"),
                getClassFilePath("testcode/gadget/cachedata/FileCacheData"),
                getClassFilePath("testcode/gadget/cachedata/SpecialCacheData"),
                getClassFilePath("testcode/gadget/SuperMap")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        try(Transaction tx = db.beginTx()) {
            Result resAllNodes = db.execute("MATCH (n:Variable) RETURN n;", HashMapBuilder.buildObj());

            boolean foundExec_p3 = false;
            boolean foundExec_p4 = false;
            boolean foundRuntime_p0 = false;

            for (Map<String,Object> node : iterable(resAllNodes)) {
                Node n = (Node)node.get("n");
                //System.out.println(printNode(n));

                Iterator<Label> lbls = n.getLabels().iterator();
                Label lbl = null;
                if(lbls.hasNext()) {
                    lbl = lbls.next();
                }
                if(lbl.name().equals("Variable")) {
                    if(n.getProperty("name").equals("testcode/gadget/cachedata/SpecialCacheData.executeCommand(Ljava/lang/String;IJ)Ljava/lang/String;_p3")) { //String parameter
                        foundExec_p3 = true;
                    }
                    else if(n.getProperty("name").equals("testcode/gadget/cachedata/SpecialCacheData.executeCommand(Ljava/lang/String;IJ)Ljava/lang/String;_p4")) { //"this" parameter
                        foundExec_p4 = true;
                    }
                    else if(n.getProperty("name").equals("java/lang/Runtime.exec(Ljava/lang/String;)Ljava/lang/Process;_p0")) { //String parameter
                        foundRuntime_p0 = true;
                    }
                }
            }

            assertTrue(foundExec_p3,"Missing parameter");
            assertTrue(foundExec_p4,"Missing parameter");
            assertTrue(foundRuntime_p0,"Missing parameter");


            //Verify source to sink link with TRANSFER relationship
            int source2sink = getNodeCount("MATCH (source:Variable)-[r:TRANSFER*0..5]->(sink:Variable)\n" + //
                    "WHERE \n" + //
                    "  sink.name = $sink AND\n" + //
                    "  source.name = $source\n" + //
                    "RETURN source,sink,r;",db);
            assertTrue(source2sink>0,"Path from executeCommand() to Runtime.exec() not found");

            int nbVariables = getNodeCount("MATCH (n:Variable) RETURN n;",db);
            int nbFunctions = getNodeCount("MATCH (n:Function) RETURN n;",db);
            int nbClasses   = getNodeCount("MATCH (n:Class) RETURN n;",db);

            System.out.println(String.format("%d %d %d",nbVariables,nbFunctions,nbClasses));

            assertTrue(nbVariables >= 20);
            assertTrue(nbFunctions >= 30);
            assertTrue(nbClasses   >= 18);

            tx.success();
        }

        GraphBuilder.clearCache();
    }

    private int getNodeCount(String query, GraphDatabaseService db) {
        Result resNodes = db.execute( query, //
                HashMapBuilder.buildObj( //
                        "sink","java/lang/Runtime.exec(Ljava/lang/String;)Ljava/lang/Process;_p0" //
                        ,"source","testcode/gadget/cachedata/SpecialCacheData.executeCommand(Ljava/lang/String;IJ)Ljava/lang/String;_p3")
        );
        int count = 0;
        for (Map<String,Object> node : iterable(resNodes)) {
            //System.out.println(printNode((Node) node.get("n")));
            count++;
        }
        return count;
    }

    @Test
    public void analyzeWebApp() throws Exception {
//        FindSecBugsGlobalConfig.getInstance().setDebugPrintInstructionVisited(true);
//        FindSecBugsGlobalConfig.getInstance().setDebugTaintState(true);


        File tempDb = TempDatabase.createTempDirectory();
        GraphDatabaseService db = GraphInstance.getInstance().init(tempDb.getCanonicalPath());


        //Locate test code
        String[] files = {
                getClassFilePath("testcode/graph/Application"),
                getClassFilePath("testcode/graph/MainStart"),
                getClassFilePath("testcode/graph/WelcomeController"),
                getClassFilePath("testcode/graph/XmlService"),
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        try(Transaction tx = db.beginTx()) {
            int nbVariables = getNodeCount("MATCH (n:Variable) RETURN n;",db);
            int nbFunctions = getNodeCount("MATCH (n:Function) RETURN n;",db);
            int nbClasses   = getNodeCount("MATCH (n:Class) RETURN n;",db);

            System.out.println(String.format("%d %d %d",nbVariables,nbFunctions,nbClasses));

            assertTrue(nbVariables >= 15);
            assertTrue(nbFunctions >= 24);
            assertTrue(nbClasses   >= 17);
            tx.success();
        }


        GraphBuilder.clearCache();
    }
}
