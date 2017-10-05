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

import com.h3xstream.findsecbugs.graph.model.RelTypes;
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrameAdditionalVisitor;
import edu.umd.cs.findbugs.BugReporter;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.LoadInstruction;
import org.apache.bcel.generic.MethodGen;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class GraphBuilder extends BasicInjectionDetector implements TaintFrameAdditionalVisitor {
    private static boolean dbInit = false;
    private static GraphDatabaseService graphDb;

    public GraphBuilder(BugReporter bugReporter) {
        super(bugReporter);

        registerVisitor(this);
        if(!dbInit) {
            dbInit = true;
            init();
        }
    }

    private void init() {
        File dbLocation = new File("codegraph.db");
        System.out.println("Graph db created : "+dbLocation.toString());
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(dbLocation);
        Runtime.getRuntime().addShutdownHook( new Thread() {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }

    @Override
    public void visitInvoke(InvokeInstruction invoke, ConstantPoolGen cpg, MethodGen methodGen, TaintFrame frame) {


        Label lbl = Label.label("Function");
        Set<Integer> relationship = new HashSet<>();

        try (Transaction tx = graphDb.beginTx()) {

            //Source
            String sourceClass = methodGen.getClassName().replaceAll("\\.", "/");
            String sourceCall = sourceClass + "." + methodGen.getName() + methodGen.getSignature();
            //Target
            String invokeClass = invoke.getClassName(cpg).replaceAll("\\.", "/");
            String invokeCall = invokeClass + "." + invoke.getMethodName(cpg) + invoke.getSignature(cpg);

            //System.out.println("[DEBUG] "+sourceCall + " -> " + invokeCall);


            Node sourceNode = graphDb.findNode(lbl, "name", sourceCall);
            Node invokeNode = graphDb.findNode(lbl, "name", invokeCall);

            if(sourceNode == null) {
                sourceNode = graphDb.createNode(lbl);
                sourceNode.setProperty("name", sourceCall);
                sourceNode.setProperty("class", sourceClass);
                sourceNode.setProperty("function", methodGen.getName());
            }
            if(invokeNode == null) {
                invokeNode = graphDb.createNode(lbl);
                invokeNode.setProperty("name", invokeCall);
                invokeNode.setProperty("class", invokeClass);
                invokeNode.setProperty("function", invoke.getMethodName(cpg));
            }

            //int hashCode = sourceCall.hashCode() & invokeCall.hashCode();
            //if(!relationship.contains(hashCode))
            if(!hasRelationship(sourceNode,invokeNode))
            {
                sourceNode.createRelationshipTo(invokeNode, RelTypes.CALL);
                System.out.println("Create link "+sourceCall + " -> " + invokeCall);
//                relationship.add(hashCode);
            }

            tx.success();
        }
    }

    @Override
    public void visitLoad(LoadInstruction instruction, ConstantPoolGen cpg, MethodGen methodGen, TaintFrame frame, int numProduced) {

    }

    private boolean hasRelationship(Node fromNode,Node toNode) {
        if (fromNode.hasRelationship(Direction.OUTGOING, RelTypes.CALL)) {
            Iterable<Relationship> relations = fromNode.getRelationships(Direction.OUTGOING, RelTypes.CALL);
            for (Relationship relation : relations) {
                if (relation.getEndNode().equals(toNode)) {
                    return true;
                }
            }
        }
        return false;
    }

}
