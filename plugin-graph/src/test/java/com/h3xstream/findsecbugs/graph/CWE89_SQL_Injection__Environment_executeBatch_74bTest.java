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
import static com.h3xstream.findsecbugs.graph.util.GraphQueryUtil.printNode;
import static org.mockito.Mockito.spy;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class CWE89_SQL_Injection__Environment_executeBatch_74bTest extends BaseGraphDetectorTest {

    @Test
    public void analyzeFieldToSink() throws Exception {

        //Locate test code
        String[] files = {
                getClassFilePath("testcases.CWE89_SQL_Injection.s01.CWE89_SQL_Injection__Environment_executeBatch_74a"),
                getClassFilePath("testcases.CWE89_SQL_Injection.s01.CWE89_SQL_Injection__Environment_executeBatch_74b")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        try(Transaction tx = db.beginTx()) {

            { //The hashmap built in 74a should be consider safe (all constant value)

                String query = "MATCH (source:Variable)-[r1:TRANSFER*0..8]->(node:Variable)-[r:TRANSFER]->(sink:Variable)\n" +
                        "WHERE\n" +
                        "  source.state = \"SAFE\" AND\n" +
                        "  sink.name = \"java/sql/Statement.addBatch(Ljava/lang/String;)V_p0\" AND\n" +
                        "  node.source = \"testcases/CWE89_SQL_Injection/s01/CWE89_SQL_Injection__Environment_executeBatch_74b.goodG2BSink(Ljava/util/HashMap;)V\"\n" +
                        "RETURN source,sink,r1,node,r;";
                Result res = db.execute(query,
                        HashMapBuilder.buildObj());
                System.out.println(query);

                assertTrue(iterable(res).iterator().hasNext());

                int count = 0;
                Node fromNode = getNodeByName("java/util/HashMap.put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;_ret",db);
                Node toNode   = getNodeByName("testcases/CWE89_SQL_Injection/s01/CWE89_SQL_Injection__Environment_executeBatch_74b.goodG2BSink(Ljava/util/HashMap;)V_p0",db);
                String source = "testcases/CWE89_SQL_Injection/s01/CWE89_SQL_Injection__Environment_executeBatch_74a.goodG2B()V";
                boolean found = false;
                boolean fromNodeFound = false;
                boolean toNodeFound = false;
                for (Map<String, Object> node : iterable(res)) {
                    count++;
                    Node n = (Node) node.get("source");
                    System.out.println(printNode(n));
                    for(Relationship rel : n.getRelationships(Direction.INCOMING)) {
                        Node startNode = rel.getStartNode();
                        if(startNode.equals(fromNode)) {
                            fromNodeFound = true;
                        }
                    }
                    for(Relationship rel : n.getRelationships(Direction.OUTGOING)) {
                        Node endNode = rel.getEndNode();
                        if(endNode.equals(toNode)) {
                            toNodeFound = true;
                        }
                    }
                    if(n.getProperty("source").equals(source)) {
                        found = true;
                    }
                }
                assertTrue(found, "The HashMap with constant value was not marked as safe");
                assertTrue(toNodeFound || fromNodeFound, "The intermediate node does not have the proper link");
                assertEquals(count,3, "At least two safe variables should lead to the sink");
            }


            { //No tainted variable should be able to reach the addBatch parameter index 0
                Result res = db.execute("MATCH (source:Variable)-[r1:TRANSFER*0..8]->(node:Variable)-[r:TRANSFER]->(sink:Variable)\n" +
                                "WHERE\n" +
                                "  source.state = \"TAINTED\" AND\n" +
                                "  sink.name = \"java/sql/Statement.addBatch(Ljava/lang/String;)V_p0\" AND\n" +
                                "  node.source = \"testcases/CWE89_SQL_Injection/s01/CWE89_SQL_Injection__Environment_executeBatch_74b.goodG2BSink(Ljava/util/HashMap;)V\"\n" +
                                "RETURN source,sink,r1,node,r;",
                        HashMapBuilder.buildObj());

                assertFalse(iterable(res).iterator().hasNext());
            }

            tx.success();
        }
    }

    public Node getNodeByName(String name,GraphDatabaseService db) {
        Result res = db.execute("MATCH (n:Variable{name:$name}) RETURN n;", HashMapBuilder.buildObj("name",name));

        assertTrue(iterable(res).iterator().hasNext());

        for (Map<String, Object> node : iterable(res)) {
            Node n = (Node) node.get("n");
            return n;
        }
        throw new RuntimeException("Node not found: "+name);
    }
}
