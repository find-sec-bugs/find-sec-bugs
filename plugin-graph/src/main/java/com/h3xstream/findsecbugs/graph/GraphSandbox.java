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

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.Map;

public class GraphSandbox {
    
    public static void main(String[] args){
        File dbLocation = new File("codegraph.db");
        System.out.println("Graph db created : "+dbLocation.toString());
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(dbLocation);

        Runtime.getRuntime().addShutdownHook( new Thread() {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }
}
