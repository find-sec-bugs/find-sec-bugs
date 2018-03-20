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
import org.testng.annotations.Test;

import java.io.File;
import java.util.Map;

import static com.h3xstream.findsecbugs.graph.util.GraphQueryUtil.iterable;
import static org.mockito.Mockito.spy;
import static org.testng.Assert.assertTrue;

/**
 * This test case validate a fix made to the graph construction (cache of relationship).
 * The previous implementation was subject to collision.
 *
 * The first method test a very specific occurrence.
 * The second test with a the complete Struts core jar which is use as example. The problem affected any large code base.
 */
public class ClassPathFinderSampleTest extends BaseGraphDetectorTest {

    @Test
    public void analyzeStrutsClassPathFinderSample() throws Exception {

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/special/ClassPathFinderSample")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        try(Transaction tx = db.beginTx()) {
            {
                Result resAllNodes = db.execute("MATCH (n:Variable) RETURN n;", HashMapBuilder.buildObj());

                for (Map<String, Object> node : iterable(resAllNodes)) {
                    Node n = (Node) node.get("n");
                    //System.out.println(printNode(n));
                }
            }

            //Check specific file handle
            {
                Result res = db.execute("MATCH (n:Variable{name:\"java/io/File.<init>(Ljava/io/File;Ljava/lang/String;)V_p0\"})\n" +
                        "RETURN n;", HashMapBuilder.buildObj());

                assertTrue(iterable(res).iterator().hasNext());
            }
            {
                String sink = "java/io/File.<init>(Ljava/io/File;Ljava/lang/String;)V_p0";
                String source = "testcode/special/ClassPathFinderSample.checkEntries([Ljava/lang/String;Ljava/io/File;Ljava/lang/String;)Ljava/util/Vector;";

                Result res = db.execute("MATCH (source:Variable)-[r1:TRANSFER*0..5]->(node:Variable)-[r:TRANSFER]->(sink:Variable)\n" +
                        "WHERE source.state = \"UNKNOWN\" AND\n" +
                        "  sink.name = $sink AND\n" +
                        "  node.source = $source\n" +
                        "RETURN source,sink,r1,node,r;", HashMapBuilder.buildObj("source", source, "sink", sink));

                assertTrue(iterable(res).iterator().hasNext(), String.format("Could not find the source '%s'", source));
            }
            {
                String sink = "java/io/File.<init>(Ljava/net/URI;)V_p0";
                String source = "testcode/special/ClassPathFinderSample.findMatches()Ljava/util/Vector;";

                Result res = db.execute("MATCH (source:Variable)-[r1:TRANSFER*0..5]->(node:Variable)-[r:TRANSFER]->(sink:Variable)\n" +
                        "WHERE source.state = \"UNKNOWN\" AND\n" +
                        "  sink.name = $sink AND\n" +
                        "  node.source = $source\n" +
                        "RETURN source,sink,r1,node,r;", HashMapBuilder.buildObj("source", source, "sink", sink));

                assertTrue(iterable(res).iterator().hasNext(), String.format("Could not find the source '%s'", source));
            }

            tx.success();
        }
    }


    @Test(enabled = false)
    public void analyzeStrutsClassPathFinderComplete() throws Exception {

        //Locate test code
        String[] files = {
                getClassFilePath("samples/struts2-core-2.5.14-SNAPSHOT.jar")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        try(Transaction tx = db.beginTx()) {
            {
                Result resAllNodes = db.execute("MATCH (n:Variable) RETURN n;", HashMapBuilder.buildObj());

                for (Map<String, Object> node : iterable(resAllNodes)) {
                    Node n = (Node) node.get("n");
                    //System.out.println(printNode(n));
                }
            }

            //Check specific file handle
            {
                Result res = db.execute("MATCH (n:Variable{name:\"java/io/File.<init>(Ljava/io/File;Ljava/lang/String;)V_p0\"})\n" +
                        "RETURN n;", HashMapBuilder.buildObj());

                assertTrue(iterable(res).iterator().hasNext());
            }

            {
                String sink = "java/io/File.<init>(Ljava/io/File;Ljava/lang/String;)V_p0";
                String[] sources = { //
                        "com/opensymphony/xwork2/util/ClassPathFinder.checkEntries([Ljava/lang/String;Ljava/io/File;Ljava/lang/String;)Ljava/util/Vector;", //
                        "com/opensymphony/xwork2/util/classloader/FileResourceStore.getFile(Ljava/lang/String;)Ljava/io/File;", //
                        //,
                };

                for (String source : sources) {
                    Result res = db.execute("MATCH (source:Variable)-[r1:TRANSFER*0..5]->(node:Variable)-[r:TRANSFER]->(sink:Variable)\n" +
                            "WHERE source.state = \"UNKNOWN\" AND\n" +
                            "  sink.name = $sink AND\n" +
                            "  node.source = $source\n" +
                            "RETURN source,sink,r1,node,r;", HashMapBuilder.buildObj("source", source, "sink", sink));

                    assertTrue(iterable(res).iterator().hasNext(), String.format("Could not find the source '%s'", source));
                }
            }

            {
                String source = "com/opensymphony/xwork2/util/ClassPathFinder.findMatches()Ljava/util/Vector;";
                String sink   = "java/io/File.<init>(Ljava/net/URI;)V_p0";
                Result res = db.execute("MATCH (source:Variable)-[r1:TRANSFER*0..5]->(node:Variable)-[r:TRANSFER]->(sink:Variable)\n" +
                        "WHERE source.state = \"UNKNOWN\" AND\n" +
                        "  sink.name = $sink AND\n" +
                        "  node.source = $source\n" +
                        "RETURN source,sink,r1,node,r;", HashMapBuilder.buildObj("source",source,"sink",sink));

                assertTrue(iterable(res).iterator().hasNext(),String.format("Could not find the source '%s'",source));
            }
            {
                String source = "com/opensymphony/xwork2/util/finder/ResourceFinder.readDirectoryEntries(Ljava/net/URL;Ljava/util/Map;)V";
                String sink   = "java/io/File.<init>(Ljava/lang/String;)V_p0";
                Result res = db.execute("MATCH (source:Variable)-[r1:TRANSFER*0..5]->(node:Variable)-[r:TRANSFER]->(sink:Variable)\n" +
                        "WHERE source.state = \"UNKNOWN\" AND\n" +
                        "  sink.name = $sink AND\n" +
                        "  node.source = $source\n" +
                        "RETURN source,sink,r1,node,r;", HashMapBuilder.buildObj("source",source,"sink",sink));

                assertTrue(iterable(res).iterator().hasNext(),String.format("Could not find the source '%s'",source));
            }
            tx.success();
        }
    }
}
