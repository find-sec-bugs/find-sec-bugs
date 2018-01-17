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
package com.h3xstream.findsecbugs.graph.util;

import com.h3xstream.findsecbugs.graph.HashMapBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.util.*;

public class GraphQueryUtil {

    public static List<Map<String,Object>> queryGraph(String query, Map<String,Object> params, GraphDatabaseService graphDb) {
        try(Transaction tx = graphDb.beginTx()) {
            Result res = graphDb.execute(query, params);
            tx.success();

            List<Map<String,Object>> nodes = new ArrayList<Map<String,Object>>();
            for (Map<String,Object> node : iterable(res)) {
                nodes.add(node);
            }
            return nodes;
        }
    }


    public static<T> Iterable<T> iterable(Iterator<T> iterator) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return iterator;
            }
        };
    }


    public static String printNode(Node node) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Node: "+Arrays.asList(node.getLabels()).toString()+"\n");

        for(Map.Entry<String,Object> value : node.getAllProperties().entrySet()) {
            buffer.append(" - "+value.getKey() + ": " + value.getValue()+"\n");
        }
        return buffer.toString();
    }

    public static int getNodeCount(String query,GraphDatabaseService db) {
        return getNodeCount(query, HashMapBuilder.buildObj(),db);
    }
    public static int getNodeCount(String query,Map<String, Object> params, GraphDatabaseService db) {
        Result resNodes = db.execute( query, params   );
        int count = 0;
        for (Map<String,Object> node : iterable(resNodes)) {
            //System.out.println(printNode((Node) node.get("n")));
            count++;
        }
        return count;
    }

}
