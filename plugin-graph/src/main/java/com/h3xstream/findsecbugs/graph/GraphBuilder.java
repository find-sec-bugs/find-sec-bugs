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

import com.h3xstream.findsecbugs.graph.model.GraphLabels;
import com.h3xstream.findsecbugs.graph.model.RelTypes;
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrameAdditionalVisitor;
import com.h3xstream.findsecbugs.taintanalysis.data.UnknownSource;
import com.h3xstream.findsecbugs.taintanalysis.data.UnknownSourceType;
import edu.umd.cs.findbugs.BugReporter;
import org.apache.bcel.generic.*;
import org.neo4j.graphdb.*;

import java.util.*;

import static com.h3xstream.findsecbugs.graph.HashMapBuilder.*;

public class GraphBuilder extends BasicInjectionDetector implements TaintFrameAdditionalVisitor {

    /**
     * Do NOT store GraphDatabaseService directly. It would limit the capability to switch db in unit test.
     */
    private static GraphInstance graphDb;

    private static List<String> EXCLUDED_PACKAGES = Arrays.asList("java/", "javax/");
    private static List<String> INCLUDE_JAVA_API = Arrays.asList(
            "java/sql/","javax/persistence/", //SQLi
            "java/lang/Runtime","java/lang/ProcessBuilder", //CmdExec
            "java/io/File","java/io/RandomFile","java/io/FileReader","java/io/FileInputStream","java/nio/file/Paths","java/io/FileWriter", "java/io/FileOutputStream","java/net/URL", //Path traversal
            "java/io/PrintWriter", "javax/servlet/", //XSS
            "javax/naming/directory/", "javax/naming/ldap"); //LDAP

    public GraphBuilder(BugReporter bugReporter) {
        super(bugReporter);

        registerVisitor(this);

        graphDb = GraphInstance.getInstance();
    }


    @Override
    public void visitInvoke(InvokeInstruction invoke, MethodGen methodGen, TaintFrame frame, List<Taint> parameters, ConstantPoolGen cpg) throws ClassNotFoundException {
        GraphDatabaseService db = graphDb.getDb();
        try (Transaction tx = db.beginTx()) {

            //Source method
            String sourceClass = getSlashClassName(methodGen.getClassName());
            String sourceCall = sourceClass + "." + methodGen.getName() + methodGen.getSignature();

            //Target method
            String invokeClass = getSlashClassName(invoke.getClassName(cpg));
            String invokeCall = invokeClass + "." + invoke.getMethodName(cpg) + invoke.getSignature(cpg);

            /////
            //Create variable nodes

            //Skip some API to reduce the graph size
            boolean isExclude = false;
            for (String exclPackage: EXCLUDED_PACKAGES) {
                if(invokeClass.startsWith(exclPackage)) {
                    isExclude = true;
                }
            }
            boolean isInclude = false;
            for (String exclPackage: INCLUDE_JAVA_API) {
                if(invokeClass.startsWith(exclPackage)) {
                    isInclude = true;
                }
            }
            if(!isExclude || isInclude) {
                //ByteCode.printOpCode(invoke,cpg);
                int destParamIndex = 0;
                for (Taint param : parameters) {

                    if(param.getSources().size() > 0) {
                        //Destination
                        String destParamKey = invokeCall + "_p" + destParamIndex;

                        //
                        Node destParamNode = createNode(GraphLabels.LABEL_VARIABLE,
                                "name", destParamKey,
                                "state", param.getState().name(),
                                "type", "P");

                        //Source
                        for(UnknownSource source : param.getSources()) {
                            linkSourceToNode(source, destParamNode, sourceCall);
                        }

                    }
                    destParamIndex++;
                }
            }



            tx.success();
        }
    }

    @Override
    public void visitReturn(MethodGen methodGen, Taint returnValue, ConstantPoolGen cpg) throws Exception {
        GraphDatabaseService db = graphDb.getDb();
        try (Transaction tx = db.beginTx()) {
            //Source method
            String sourceClass = getSlashClassName(methodGen.getClassName());
            String sourceCall = sourceClass + "." + methodGen.getName() + methodGen.getSignature();

            if(returnValue != null) { //Skipping return void
                if (returnValue.getSources().size() > 0) {
                    //Destination
                    String destParamKey = sourceCall + "_ret";

                    //Multiple return instruction can be place in the same method. Therefore we can't hold the state in this specific node.
                    Node destParamNode = createNode(GraphLabels.LABEL_VARIABLE,
                            "name", destParamKey,
                            //"state", returnValue.getState().name(),
                            "type", "R");

                    //Source
                    for (UnknownSource source : returnValue.getSources()) {
                        linkSourceToNode(source, destParamNode, sourceCall);
                    }
                    tx.success();
                }
            }
        }
    }

    @Override
    public void visitLoad(LoadInstruction instruction, MethodGen methodGen, TaintFrame frame, int numProduced, ConstantPoolGen cpg) {

    }

    @Override
    public void visitField(FieldInstruction store, MethodGen methodGen, TaintFrame frameType, Taint fieldValue, int numProduced, ConstantPoolGen cpg) throws Exception {
        GraphDatabaseService db = graphDb.getDb();
        try (Transaction tx = db.beginTx()) {
            //Source method
            String sourceClass = getSlashClassName(methodGen.getClassName());
            String sourceCall = sourceClass + "." + methodGen.getName() + methodGen.getSignature();

            //Value load or store (fieldValue)
            if(fieldValue.getSources().size() > 0) {
                if(store instanceof PUTFIELD || store instanceof PUTSTATIC) {
                    //Destination
                    String destParamKey = getSlashClassName(store.getClassName(cpg))+"."+store.getFieldName(cpg);

                    //
                    Node destParamNode = createNode(GraphLabels.LABEL_VARIABLE,
                            "name", destParamKey,
                            //"state", returnValue.getState().name(),
                            "type", "F");
                    //Source
                    for(UnknownSource source : fieldValue.getSources()) {
                        linkSourceToNode(source, destParamNode, sourceCall);
                    }
                }
            }
            tx.success();
        }
    }


    private void linkSourceToNode(UnknownSource source, Node destParamNode, String sourceCall) {
        UnknownSourceType type = source.getSourceType();
        switch (type) {
            case FIELD: // Field -TRANSFER-> node

                String field = source.getSignatureField();

                Node srcFieldNode = createNode(GraphLabels.LABEL_VARIABLE,
                        "name", field,
                        //"state", source.getState().name(),
                        "type", "F");

                createExternalCallRelationship(srcFieldNode, destParamNode, RelTypes.TRANSFER, sourceCall, source);

                break;
            case PARAMETER: // Parameter -TRANSFER-> node

                int sourceParamIndex = source.getParameterIndex();
                String srcParamKey = sourceCall + "_p"+ sourceParamIndex;


                Node srcParamNode = createNode(GraphLabels.LABEL_VARIABLE,
                        "name", srcParamKey,
                        //"state", source.getState().name(),
                        "type", "P");

                createExternalCallRelationship(srcParamNode, destParamNode, RelTypes.TRANSFER, sourceCall, source);

                break;

            case RETURN: // return value -TRANSFER-> node

                String srcMethodKey = source.getSignatureMethod()+"_ret";

                Node srcMethodNode = createNode(GraphLabels.LABEL_VARIABLE,
                        "name", srcMethodKey,
                        //"state", source.getState().name(),
                        "type", "R");

                createExternalCallRelationship(srcMethodNode, destParamNode, RelTypes.TRANSFER, sourceCall, source);

                break;
        }
    }

    /**
     * This function is creating a node if does not exist already.
     * It use an map to keep track of previously loaded node rather communicating with Neo4j.
     *
     * @param lbl
     * @param properties
     * @return
     */
    private Node createNode(Label lbl, String... properties) {
        Map<String, Node> cache = graphDb.getNodesCache();

        Map<String,String> props = build(properties);
        String name = props.get("name");

        //Node node = graphDb.findNode(LABEL_VARIABLE, "name", props.get("name"));
        //The cache is used because findNode seems do be a cursor that is not scaling on large library
        Node node = cache.get(name);

        if(node == null) { //Node is not found so we create it
            node = graphDb.getDb().createNode(lbl);
            for(Map.Entry<String,String> prop : props.entrySet()) {
                node.setProperty(prop.getKey(), prop.getValue());
            }
            cache.put(name, node);
            //System.out.println("Node created : "+props.get("name"));
        }

        return node;
    }

    private String getSlashClassName(String dottedClassName) {
        return dottedClassName.replaceAll("\\.", "/");
    }

    private void createExternalCallRelationship(Node fromNode,Node toNode, RelationshipType rt, String sourceCall,UnknownSource source) {

        String from = (String) fromNode.getProperty("name");
        String to   = (String) toNode.getProperty("name");

        Node intermediateNode = createNode(GraphLabels.LABEL_VARIABLE,
                "name", from+">>"+to+"!"+sourceCall, //FIXME: Not used.. but kept to keep track of node created
                //"from", from,
                //"to", to,
                "source",sourceCall,
                "state", source.getState().name(),
                "type", "I");
        createRelationship(fromNode,intermediateNode,rt,null);
        createRelationship(intermediateNode,toNode,rt,null);
    }

    /**
     * Create a relationship if it does not exist.
     * @param fromNode
     * @param toNode
     * @param rt
     */
    private void createRelationship(Node fromNode,Node toNode, RelationshipType rt, String sourceCall) {
        Set<String> relationshipCache = graphDb.getRelationshipCache();

        if(hasRelationship(fromNode, toNode, rt,sourceCall)) {
            return;
        }

        String key = getKey(fromNode,toNode,rt,sourceCall);
        relationshipCache.add(key);
        Relationship relation = fromNode.createRelationshipTo(toNode,rt);
        if(sourceCall != null) {
            relation.setProperty("source", sourceCall);
        }
    }

    private String getKey(Node fromNode,Node toNode, RelationshipType rt, String sourceCall) {
       return fromNode.getProperty("name") + "->" + toNode.getProperty("name") + "__" + rt.name() + "__" + (sourceCall == null ? "" : sourceCall);
    }

    /**
     * Look at the cache relationship. This method assume relationship were created using the createRelationship method.
     *
     * @param fromNode
     * @param toNode
     * @param rt
     * @return
     */
    private boolean hasRelationship(Node fromNode,Node toNode, RelationshipType rt, String sourceCall) {
        Set<String> relationshipCache = graphDb.getRelationshipCache();

        String key = getKey(fromNode,toNode,rt,sourceCall);
        return relationshipCache.contains(key);
    }

}
