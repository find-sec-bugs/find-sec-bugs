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
package com.h3xstream.findsecbugs.serial;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.FieldAnnotation;
import edu.umd.cs.findbugs.ba.CFG;
import edu.umd.cs.findbugs.ba.CFGBuilderException;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.ba.Location;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.ElementValuePair;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.MethodGen;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Detect unsafe Jackson datatype deserialization
 *
 */
public class UnsafeJacksonDeserializationDetector implements Detector {

    private static final String DESERIALIZATION_TYPE = "JACKSON_UNSAFE_DESERIALIZATION";

    private BugReporter bugReporter;


    public UnsafeJacksonDeserializationDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    private static final List<String> ANNOTATION_TYPES = Arrays.asList(
            "Lcom/fasterxml/jackson/annotation/JsonTypeInfo;");

    private static final List<String> VULNERABLE_USE_NAMES = Arrays.asList(
            "CLASS", "MINIMAL_CLASS");

    private static final List<String> OBJECT_MAPPER_CLASSES = Arrays.asList(
            "com.fasterxml.jackson.databind.ObjectMapper",
            "org.codehaus.jackson.map.ObjectMapper");


    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();
        if (OBJECT_MAPPER_CLASSES.contains(javaClass.getClassName())) {
            return;
        }
        for (Field field : javaClass.getFields()) {
            analyzeField(field, javaClass);
        }
        for (Method m : javaClass.getMethods()) {
            try {
                analyzeMethod(m, classContext);
            }
            catch (CFGBuilderException | DataflowAnalysisException e) {
            }
        }
    }

    private void analyzeField(Field field, JavaClass javaClass) {
        for (AnnotationEntry annotation : field.getAnnotationEntries())  {
            if (ANNOTATION_TYPES.contains(annotation.getAnnotationType()) ||
                    annotation.getAnnotationType().contains("JsonTypeInfo")) {
                for (ElementValuePair elementValuePair : annotation.getElementValuePairs()) {
                    if ("use".equals((elementValuePair.getNameString())) &&
                            VULNERABLE_USE_NAMES.contains(elementValuePair.getValue().stringifyValue())) {
                        bugReporter.reportBug(new BugInstance(this, DESERIALIZATION_TYPE, HIGH_PRIORITY)
                                .addClass(javaClass)
                                .addString(javaClass.getClassName() + " on field " +
                                        field.getName() + " of type " + field.getType() +
                                        " annotated with " + annotation.toShortString())
                                .addField(FieldAnnotation.fromBCELField(javaClass, field))
                                .addString("")
                        );
                    }
                }
            }
        }
    }

    private void analyzeMethod(Method m, ClassContext classContext) throws CFGBuilderException, DataflowAnalysisException {
        MethodGen methodGen = classContext.getMethodGen(m);
        ConstantPoolGen cpg = classContext.getConstantPoolGen();
        CFG cfg = classContext.getCFG(m);

        if (methodGen == null || methodGen.getInstructionList() == null) {
            return; //No instruction .. nothing to do
        }
        for (Iterator<Location> i = cfg.locationIterator(); i.hasNext(); ) {
            Location location = i.next();
            Instruction inst = location.getHandle().getInstruction();
            if (inst instanceof InvokeInstruction) {
                InvokeInstruction invoke = (InvokeInstruction) inst;
                String methodName = invoke.getMethodName(cpg);
                if ("enableDefaultTyping".equals(methodName)) {
                    JavaClass clz = classContext.getJavaClass();
                    bugReporter.reportBug(new BugInstance(this, DESERIALIZATION_TYPE, HIGH_PRIORITY)
                            .addClass(clz)
                            .addMethod(clz, m)
                            .addCalledMethod(cpg, invoke)
                            .addSourceLine(classContext, m, location)
                    );
                }
            }
        }
    }

    @Override
    public void report() {
    }

}
