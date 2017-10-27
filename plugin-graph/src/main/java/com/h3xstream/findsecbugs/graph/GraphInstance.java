package com.h3xstream.findsecbugs.graph;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Stack;

/**
 * Singleton to obtain the current Graph database.
 *
 * The instantiation of the GraphDatabaseService was separate from the GraphBuilder to allow "mocking" of the graph.
 */
public class GraphInstance {

    private static GraphInstance instance = new GraphInstance();

    /**
     * Even though the current Graph database can be overridden, we need to keep track of previous DB to do proper cleanup
     * at shutdown.
     */
    private Stack<GraphDatabaseService> dbs = new Stack<>();
    private Stack<File> dbsLocation = new Stack<>();

    private GraphInstance() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                for(GraphDatabaseService db : dbs) {
                    db.shutdown();
                }
                for(File location : dbsLocation) {
                    try {
                        FileUtils.deleteRecursively(location);
                    } catch (IOException e) {
                        System.out.println("Attempt to delete "+location.toString()+":"+e.getMessage());
                    }
                }
            }
        });
    }

    public static GraphInstance getInstance() {
        return instance;
    }

    public GraphDatabaseService getDb() {
        if(dbs.size() == 0) {
            init();
        }
        return dbs.peek();
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
        File dbLocation = new File(databasePath);
        System.out.println("Creating Neo4J database: " + dbLocation.toString());

        dbsLocation.push(dbLocation);
        dbs.push(new GraphDatabaseFactory().newEmbeddedDatabase(dbLocation));

        return dbs.peek();
    }
}
