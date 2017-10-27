package com.h3xstream.findsecbugs.graph.util;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.util.*;

import static com.h3xstream.findsecbugs.graph.util.GraphQueryUtil.iterable;

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
}
