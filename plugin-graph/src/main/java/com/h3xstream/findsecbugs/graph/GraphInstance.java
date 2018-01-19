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

import com.h3xstream.findsecbugs.graph.util.GraphQueryUtil;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Singleton to obtain the current Graph database.
 *
 * The instantiation of the GraphDatabaseService was separate from the GraphBuilder to allow "mocking" of the graph.
 */
public class GraphInstance {

    private static Map<String, Node> nodesCache = new HashMap<>();
    private static Set<String> relationshipCache = new HashSet<>();

    private static GraphInstance instance = new GraphInstance();

    public static boolean mustDeleteFileCreated = false;
    public static boolean mustShutdownDatabase = true; //

    private GraphDatabaseService db = null;
    private File dbFile = null;

    /**
     * Even though the current Graph database can be overridden, we need to keep track of previous DB to do proper cleanup
     * at shutdown.
     */
    private List<GraphDatabaseService> dbsCreated = new ArrayList<>();
    private List<File> dbFilesCreated = new ArrayList<>();
    /**
     * The clean up process the database
     */
    private boolean databaseRemoved = false;


    private GraphInstance() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shutdownDatabases();
            removeDatabasesFile();
        }
        ));
    }

    public synchronized void shutdownDatabases() {
        for (GraphDatabaseService db : dbsCreated) {
            if(db.isAvailable(10)) {
                db.shutdown();
            }
        }
    }

    public synchronized void removeDatabasesFile() {
        if(!databaseRemoved) {
            if (mustDeleteFileCreated) {
                for (File dbToRemove : dbFilesCreated) {
                    try {
                        System.out.println("Deleting " + dbToRemove.toString());

                        FileUtils.deleteRecursively(dbToRemove);

                    } catch (IOException e) {
                        System.err.println("Failed to delete " + dbFile.toString() + ":" + e.getMessage());
                    }
                }
            }
            databaseRemoved = true;
        }
    }

    public Map<String, Node> getNodesCache() {
        return nodesCache;
    }

    public Set<String> getRelationshipCache() {
        return relationshipCache;
    }

    public void clearDatabase() {
        nodesCache.clear();
        relationshipCache.clear();
        GraphQueryUtil.queryGraph("MATCH (n)\n" + //Ref : https://stackoverflow.com/a/33542193/89769
                "WITH n LIMIT 1000000\n" +
                "OPTIONAL MATCH (n)-[r]-()\n" +
                "DELETE n,r",new HashMap<>(),db);
    }

    public static GraphInstance getInstance() {
        return instance;
    }

    public synchronized GraphDatabaseService getDb() {
        if(db == null) {
            init();
        }
        return db;
    }

    public GraphDatabaseService init() {
        String envDbFile = System.getProperty("findsecbugs.graphdb");
        return init(envDbFile == null ? "codegraph.db": envDbFile);
    }

    /**
     * This method will be call with a different databasePath only in test environment where multiple db need to be created.
     * For isolation of samples to avoid collision when running again
     * @param databasePath Database location
     * @return
     */
    public GraphDatabaseService init(String databasePath) {
        File dbFile = new File(databasePath);
        System.out.println("Creating Neo4J database: " + dbFile.toString());

        db = new GraphDatabaseFactory().newEmbeddedDatabase(dbFile);
        dbFilesCreated.add(dbFile); //Location is kept for potential deletation in test
        dbsCreated.add(db);

        return db;
    }
}
