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
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.XClass;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.DescriptorFactory;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.LoadInstruction;
import org.apache.bcel.generic.MethodGen;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GraphBuilder extends BasicInjectionDetector implements TaintFrameAdditionalVisitor {
    private static boolean dbInit = false;
    private static GraphDatabaseService graphDb;
    private static Label lbl = Label.label("Function");
    private static Label lbl2 = Label.label("Class");
    private static Label lbl3 = Label.label("Interface");

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
    public void visitInvoke(InvokeInstruction invoke, ConstantPoolGen cpg, MethodGen methodGen, TaintFrame frame) throws ClassNotFoundException {

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
            Node sourceClassNode = graphDb.findNode(lbl2, "name", sourceClass);
            Node invokeClassNode = graphDb.findNode(lbl2, "name", invokeClass);

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
            if (sourceClassNode == null){
                sourceClassNode = graphDb.createNode(lbl2);
                sourceClassNode.setProperty("name", sourceClass);
            }
            if (invokeClassNode == null){
                invokeClassNode = graphDb.createNode(lbl2);
                invokeClassNode.setProperty("name", invokeClass);
            }

            //int hashCode = sourceCall.hashCode() & invokeCall.hashCode();
            //if(!relationship.contains(hashCode))
            if(!hasRelationship(sourceNode,invokeNode, RelTypes.CALL))
            {
                sourceNode.createRelationshipTo(invokeNode, RelTypes.CALL);
                System.out.println("Create link "+sourceCall + " -> " + invokeCall);
//                relationship.add(hashCode);
            }
            if(!hasRelationship(sourceNode, sourceClassNode, RelTypes.FROM))
            {
                sourceNode.createRelationshipTo(sourceClassNode, RelTypes.FROM);
                System.out.println("Create link "+sourceCall + " -> " + sourceClass);
//                relationship.add(hashCode);
            }
            if(!hasRelationship(invokeNode, invokeClassNode, RelTypes.FROM))
            {
                invokeNode.createRelationshipTo(invokeClassNode, RelTypes.FROM);
                System.out.println("Create link "+invokeCall + " -> " + invokeClass);
//                relationship.add(hashCode);
            }

            ClassDescriptor cdSource = DescriptorFactory.createClassDescriptorFromDottedClassName(methodGen.getClassName());
            ClassDescriptor cdInvoke = DescriptorFactory.createClassDescriptorFromDottedClassName(invoke.getClassName(cpg));

            ArrayList<ClassDescriptor> listSuperClassesSource = listOfSuperClasses(cdSource);
            ArrayList<ClassDescriptor> listSuperClassesInvoke = listOfSuperClasses(cdInvoke);

            //create extended classes nodes
            if (listSuperClassesSource.size() != 0)
                createSuperClassesNodes(sourceClassNode, listSuperClassesSource);
            if (listSuperClassesInvoke.size() != 0)
                createSuperClassesNodes(invokeClassNode, listSuperClassesInvoke);

            //create interfaces nodes
            createClassInterfaces(cdSource);
            createClassInterfaces(cdInvoke);
            if (listSuperClassesSource.size() != 0){
                for (int i = 0; i<listSuperClassesSource.size(); ++i)
                    createClassInterfaces(listSuperClassesSource.get(i));
            }
            if (listSuperClassesInvoke.size() != 0){
                for (int i = 0; i<listSuperClassesInvoke.size(); ++i)
                    createClassInterfaces(listSuperClassesInvoke.get(i));
            }

            tx.success();
        }
    }

    @Override
    public void visitLoad(LoadInstruction instruction, ConstantPoolGen cpg, MethodGen methodGen, TaintFrame frame, int numProduced) {

    }

    private ArrayList<ClassDescriptor> listOfSuperClasses(ClassDescriptor cd){
        ClassDescriptor temp = cd;
        ArrayList<ClassDescriptor> superClasses = new ArrayList<>();
        XClass xclassTemp = AnalysisContext.currentXFactory().getXClass(cd);
        if (xclassTemp != null){
            temp = xclassTemp.getSuperclassDescriptor();
        }
        if (temp != null){
            if (!temp.getClassName().equals("java/lang/Object")){
                superClasses.add(temp);
                while (temp != null && !temp.getClassName().equals("java/lang/Object")){
                    XClass xclass = AnalysisContext.currentXFactory().getXClass(temp);
                    if (xclass != null){
                        ClassDescriptor xSuper = xclass.getSuperclassDescriptor();
                        if (xSuper != null) {
                            superClasses.add(xSuper);
                        }
                        temp = xSuper;
                    }
                }
            }
        }
        return superClasses;
    }

    private void createSuperClassesNodes(Node sourceClassNode, ArrayList<ClassDescriptor> listOfSuperClasses){
        String superClassSource = listOfSuperClasses.get(0).getClassName().replaceAll("\\.", "/");
        Node superClassNode = graphDb.findNode(lbl2, "name", superClassSource);
        if (superClassNode == null){
            superClassNode = graphDb.createNode(lbl2);
            superClassNode.setProperty("name", superClassSource);
        }

        if(!hasRelationship(sourceClassNode,superClassNode, RelTypes.EXTENDS))
        {
            sourceClassNode.createRelationshipTo(superClassNode, RelTypes.EXTENDS);
            System.out.println("Create link "+ sourceClassNode.getProperty("name") + " -> " + superClassSource);
//                relationship.add(hashCode);
        }

        for (int i = 1; i<listOfSuperClasses.size(); ++i){
            String superSuperClassSource = listOfSuperClasses.get(i).getClassName().replaceAll("\\.", "/");
            Node superSuperClassNode = graphDb.findNode(lbl2, "name", superSuperClassSource);
            if(superSuperClassNode == null){
                superSuperClassNode = graphDb.createNode(lbl2);
                superSuperClassNode.setProperty("name", superSuperClassSource);
            }
            if(!hasRelationship(superClassNode,superSuperClassNode, RelTypes.EXTENDS))
            {
                superClassNode.createRelationshipTo(superSuperClassNode, RelTypes.EXTENDS);
                System.out.println("Create link "+superClassSource + " -> " + superSuperClassSource);
//                relationship.add(hashCode);
            }
            superClassSource = superSuperClassSource;
            superClassNode = superSuperClassNode;
        }
    }

    private void createClassInterfaces(ClassDescriptor sourceClass){
        String sourceClassName = sourceClass.getClassName().replaceAll("\\.", "/");
        Node sourceClassNode = graphDb.findNode(lbl2, "name", sourceClassName);
        XClass xclass = AnalysisContext.currentXFactory().getXClass(sourceClass);
        ClassDescriptor [] interfaces = xclass.getInterfaceDescriptorList();
        if (interfaces.length != 0){
             for (int i = 0 ;i<interfaces.length; ++i){
                 String sourceClassInterface = interfaces[i].getClassName().replaceAll("\\.", "/");
                 Node sourceClassInterfaceNode = graphDb.findNode(lbl3, "name", sourceClassInterface);
                 if(sourceClassInterfaceNode == null){
                     sourceClassInterfaceNode = graphDb.createNode(lbl3);
                     sourceClassInterfaceNode.setProperty("name", sourceClassInterface);
                 }
                 if(!hasRelationship(sourceClassNode,sourceClassInterfaceNode, RelTypes.IMPLEMENTS))
                 {
                     sourceClassNode.createRelationshipTo(sourceClassInterfaceNode, RelTypes.IMPLEMENTS);
                     System.out.println("Create link "+sourceClassName + " -> " + sourceClassInterface);
//                relationship.add(hashCode);
                 }
             }
        }
    }

    private boolean hasRelationship(Node fromNode,Node toNode, RelationshipType... rt) {

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
