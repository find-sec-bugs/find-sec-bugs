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
import com.h3xstream.findsecbugs.graph.util.ClassMetadata;
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrameAdditionalVisitor;
import com.h3xstream.findsecbugs.taintanalysis.data.TaintSource;
import com.h3xstream.findsecbugs.taintanalysis.data.TaintSourceType;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.XClass;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.DescriptorFactory;
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
    private static List<String> INCLUDE_JAVA_API = Arrays.asList("java/sql/Statement","java/lang/Runtime","java/lang/ProcessBuilder",
            "java/io/File","java/io/RandomFile","java/io/FileReader","java/io/FileInputStream","java/nio/file/Paths","java/io/FileWriter",
            "java/io/FileOutputStream","java/net/URL","java/io/PrintWriter","javax/servlet/http/");
    private static Map<String, Node> nodesCache = new HashMap<>();
    private static Set<String> relationshipCache = new HashSet<>();

    public GraphBuilder(BugReporter bugReporter) {
        super(bugReporter);

        registerVisitor(this);

        graphDb = GraphInstance.getInstance();
    }


    public static void clearCache() {
        nodesCache.clear();
        relationshipCache.clear();
    }

    @Override
    public void visitInvoke(InvokeInstruction invoke, ConstantPoolGen cpg, MethodGen methodGen, TaintFrame frame, List<Taint> parameters) throws ClassNotFoundException {
        GraphDatabaseService db = graphDb.getDb();
        try (Transaction tx = db.beginTx()) {

            //Source method
            String sourceClass = getSlashClassName(methodGen.getClassName());
            String sourceCall = sourceClass + "." + methodGen.getName() + methodGen.getSignature();

            //Target method
            String invokeClass = getSlashClassName(invoke.getClassName(cpg));
            String invokeCall = invokeClass + "." + invoke.getMethodName(cpg) + invoke.getSignature(cpg);

            /////
            //Create function nodes
            Node sourceNode = createNode(GraphLabels.LABEL_FUNCTION, "name", sourceCall,
                "class", sourceClass,
                "function", methodGen.getName());

            Node invokeNode = createNode(GraphLabels.LABEL_FUNCTION, "name", invokeCall,
                "class", invokeClass,
                "function", invoke.getMethodName(cpg));

            /////
            //Create class nodes
            Node sourceClassNode = createNode(GraphLabels.LABEL_CLASS, "name", sourceClass);
            Node invokeClassNode = createNode(GraphLabels.LABEL_CLASS, "name", invokeClass);

            createRelationship(sourceNode, invokeNode, RelTypes.CALL);
            createRelationship(sourceNode, sourceClassNode, RelTypes.FROM_CLASS);
            createRelationship(invokeNode, invokeClassNode, RelTypes.FROM_CLASS);


            /////
            //Create subclasses nodes

            ClassDescriptor cdSource = DescriptorFactory.createClassDescriptorFromDottedClassName(methodGen.getClassName());
            ClassDescriptor cdInvoke = DescriptorFactory.createClassDescriptorFromDottedClassName(invoke.getClassName(cpg));

            ArrayList<ClassDescriptor> listSuperClassesSource = ClassMetadata.listOfSuperClasses(cdSource);
            ArrayList<ClassDescriptor> listSuperClassesInvoke = ClassMetadata.listOfSuperClasses(cdInvoke);

            //create extended classes nodes
            if (listSuperClassesSource.size() != 0)
                createSuperClasses(sourceClassNode, listSuperClassesSource);
            if (listSuperClassesInvoke.size() != 0)
                createSuperClasses(invokeClassNode, listSuperClassesInvoke);

            //create interfaces nodes
            createInterfaces(cdSource);
            createInterfaces(cdInvoke);
            if (listSuperClassesSource.size() != 0){
                for (int i = 0; i<listSuperClassesSource.size(); ++i)
                    createInterfaces(listSuperClassesSource.get(i));
            }
            else if (listSuperClassesInvoke.size() != 0){
                for (int i = 0; i<listSuperClassesInvoke.size(); ++i)
                    createInterfaces(listSuperClassesInvoke.get(i));
            }

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
                        for(TaintSource source : param.getSources()) {
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
    public void visitReturn(InvokeInstruction invoke, ConstantPoolGen cpg, MethodGen methodGen, TaintFrame frameType) throws Exception {
        GraphDatabaseService db = graphDb.getDb();
        try (Transaction tx = db.beginTx()) {
            //Source method
            String sourceClass = getSlashClassName(methodGen.getClassName());
            String sourceCall = sourceClass + "." + methodGen.getName() + methodGen.getSignature();


            Taint returnValue = frameType.getStackValue(0);
            if(returnValue.getSources().size() > 0) {
                //Destination
                String destParamKey = sourceCall + "_ret";

                //
                Node destParamNode = createNode(GraphLabels.LABEL_VARIABLE,
                        "name", destParamKey,
                        "state", returnValue.getState().name(),
                        "type", "P");

                //Source
                for(TaintSource source : returnValue.getSources()) {
                    linkSourceToNode(source, destParamNode, sourceCall);
                }
                tx.success();
            }
        }
    }

    private void linkSourceToNode(TaintSource source, Node destParamNode, String sourceCall) {
        TaintSourceType type = source.getSourceType();
        switch (type) {
            case FIELD: // Field -TRANSFER-> node

                String field = source.getSignatureField();

                Node srcFieldNode = createNode(GraphLabels.LABEL_VARIABLE,
                        "name", field,
                        "state", source.getState().name(),
                        "type", "F");

                createRelationship(srcFieldNode, destParamNode, RelTypes.TRANSFER, sourceCall);

                break;
            case PARAMETER: // Parameter -TRANSFER-> node

                int sourceParamIndex = source.getParameterIndex();
                String srcParamKey = sourceCall + "_p"+ sourceParamIndex;


                Node srcParamNode = createNode(GraphLabels.LABEL_VARIABLE,
                        "name", srcParamKey,
                        "state", source.getState().name(),
                        "type", "P");

                createRelationship(srcParamNode, destParamNode, RelTypes.TRANSFER, sourceCall);

                break;

            case RETURN: // return value -TRANSFER-> node

                String srcMethodKey = source.getSignatureMethod()+"_ret";

                Node srcMethodNode = createNode(GraphLabels.LABEL_VARIABLE,
                        "name", srcMethodKey,
                        "state", source.getState().name(),
                        "type", "R");

                createRelationship(srcMethodNode, destParamNode, RelTypes.TRANSFER, sourceCall);

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
        Map<String,String> props = build(properties);
        String name = props.get("name");

        //Node node = graphDb.findNode(LABEL_VARIABLE, "name", props.get("name"));
        //The cache is used because findNode seems do be a cursor that is not scaling on large library
        Node node = nodesCache.get(name);

        if(node == null) { //Node is not found so we create it
            node = graphDb.getDb().createNode(lbl);
            for(Map.Entry<String,String> prop : props.entrySet()) {
                node.setProperty(prop.getKey(), prop.getValue());
            }
            nodesCache.put(name, node);
        }

        return node;
    }



    @Override
    public void visitLoad(LoadInstruction instruction, ConstantPoolGen cpg, MethodGen methodGen, TaintFrame frame, int numProduced) {

    }

    @Override
    public void visitField(FieldInstruction store, ConstantPoolGen cpg, MethodGen methodGen, TaintFrame frameType, int numProduced) throws Exception {
        GraphDatabaseService db = graphDb.getDb();
        try (Transaction tx = db.beginTx()) {
            //Source method
            String sourceClass = getSlashClassName(methodGen.getClassName());
            String sourceCall = sourceClass + "." + methodGen.getName() + methodGen.getSignature();


            Taint returnValue = frameType.getStackValue(0);
            if(returnValue.getSources().size() > 0) {
                //Destination
                String destParamKey = store.getName();

                //
                Node destParamNode = createNode(GraphLabels.LABEL_VARIABLE,
                        "name", destParamKey,
                        "state", returnValue.getState().name(),
                        "type", "F");

                //Source
                for(TaintSource source : returnValue.getSources()) {
                    linkSourceToNode(source, destParamNode, sourceCall);
                }
                tx.success();
            }
        }
    }

    private void createSuperClasses(Node sourceClassNode, List<ClassDescriptor> listOfSuperClasses){
        String superClassSource = getSlashClassName(listOfSuperClasses.get(0).getClassName());
        Node superClassNode = createNode(GraphLabels.LABEL_CLASS, "name", superClassSource);

        createRelationship(sourceClassNode, superClassNode, RelTypes.EXTENDS);

        for (int i = 1; i<listOfSuperClasses.size(); ++i) {
            String superSuperClassSource = getSlashClassName(listOfSuperClasses.get(i).getClassName());
            Node superSuperClassNode = createNode(GraphLabels.LABEL_CLASS, "name", superSuperClassSource);

            createRelationship(superClassNode,superSuperClassNode, RelTypes.EXTENDS);
            superClassNode = superSuperClassNode;
        }
    }

    private void createInterfaces(ClassDescriptor sourceClass) {
        String sourceClassName = getSlashClassName(sourceClass.getClassName());
        Node sourceClassNode = createNode(GraphLabels.LABEL_CLASS, "name", sourceClassName);
        XClass xclass = AnalysisContext.currentXFactory().getXClass(sourceClass);
        if (xclass != null) {
            ClassDescriptor[] interfaces = xclass.getInterfaceDescriptorList();
            if (interfaces.length != 0) {
                for (int i = 0; i < interfaces.length; ++i) {
                    String sourceClassInterface = getSlashClassName(interfaces[i].getClassName());
                    Node sourceClassInterfaceNode = createNode(GraphLabels.LABEL_INTERFACE, "name", sourceClassInterface);

                    createRelationship(sourceClassNode,sourceClassInterfaceNode, RelTypes.IMPLEMENTS);
                }
            }
        }
    }

    private String getSlashClassName(String dottedClassName) {
        return dottedClassName.replaceAll("\\.", "/");
    }

    private void createRelationship(Node fromNode,Node toNode, RelationshipType rt) {
        createRelationship(fromNode,toNode,rt,null);
    }

    /**
     * Create a relationship if it does not exist.
     * @param fromNode
     * @param toNode
     * @param rt
     */
    private void createRelationship(Node fromNode,Node toNode, RelationshipType rt, String sourceCall) {
        if(hasRelationship(fromNode, toNode, rt,sourceCall)) {
            return;
        }

        //int key = Objects.hash(fromNode.getProperty("name").hashCode(), toNode.getProperty("name"), sourceCall);
        String key = getKey(fromNode,toNode,rt,sourceCall);
        relationshipCache.add(key);
        Relationship relation = fromNode.createRelationshipTo(toNode,rt);
        if(sourceCall != null) {
            relation.setProperty("source", sourceCall);
        }
    }

    private String getKey(Node fromNode,Node toNode, RelationshipType rt, String sourceCall) {
       return new StringBuilder((String) fromNode.getProperty("name")).append("->").append(toNode.getProperty("name")).append("__").append(rt.name()).append("__").append(sourceCall==null?"":sourceCall).toString();
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

        //int key = Objects.hash(fromNode.getProperty("name"), toNode.getProperty("name"),sourceCall);
        String key = getKey(fromNode,toNode,rt,sourceCall);
        if (relationshipCache.contains(key)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This function is
     * @param fromNode
     * @param toNode
     * @param rt
     * @return
     */
    private boolean hasRelationshipRemote(Node fromNode,Node toNode, RelationshipType... rt) {
        if (fromNode.hasRelationship(Direction.OUTGOING, rt)) {
            Iterable<Relationship> relations = fromNode.getRelationships(Direction.OUTGOING, rt);
            for (Relationship relation : relations) {
                if (relation.getEndNode().equals(toNode)) {
                    return true;
                }
            }
        }
        return false;
    }

}
