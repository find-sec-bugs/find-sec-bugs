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
package com.h3xstream.findsecbugs.spring;


import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.CFG;
import edu.umd.cs.findbugs.ba.CFGBuilderException;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.Location;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.LDC;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SpringUnvalidatedRedirectDetector implements Detector {
    private static final String SPRING_UNVALIDATED_REDIRECT_TYPE = "SPRING_UNVALIDATED_REDIRECT";
    private static final List<String> REQUEST_MAPPING_ANNOTATION_TYPES = Arrays.asList(
            "Lorg/springframework/web/bind/annotation/RequestMapping;", //
            "Lorg/springframework/web/bind/annotation/GetMapping;", //
            "Lorg/springframework/web/bind/annotation/PostMapping;", //
            "Lorg/springframework/web/bind/annotation/PutMapping;", //
            "Lorg/springframework/web/bind/annotation/DeleteMapping;", //
            "Lorg/springframework/web/bind/annotation/PatchMapping;");

    private BugReporter reporter;

    public SpringUnvalidatedRedirectDetector(BugReporter bugReporter) {
        this.reporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass clazz = classContext.getJavaClass();

        if (hasRequestMapping(clazz)) {
            Method[] methods = clazz.getMethods();
            for (Method m: methods) {

                try {
                    analyzeMethod(m, classContext);
                } catch (CFGBuilderException e){
                }
            }
        }
    }

    private boolean hasRequestMapping(JavaClass clazz) {
        Method[] methods = clazz.getMethods();
        for (Method m: methods) {
            AnnotationEntry[] annotations = m.getAnnotationEntries();

            for (AnnotationEntry ae: annotations) {
                if (REQUEST_MAPPING_ANNOTATION_TYPES.contains(ae.getAnnotationType())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void analyzeMethod(Method m, ClassContext classContext) throws CFGBuilderException{
        JavaClass clazz = classContext.getJavaClass();
        ConstantPoolGen cpg = classContext.getConstantPoolGen();
        CFG cfg = classContext.getCFG(m);

        for (Iterator<Location> i = cfg.locationIterator(); i.hasNext(); ) {
            Location loc = i.next();
            Instruction inst = loc.getHandle().getInstruction();

            if (inst instanceof INVOKEVIRTUAL) {
                INVOKEVIRTUAL invoke = (INVOKEVIRTUAL)inst;
                if( "java.lang.StringBuilder".equals(invoke.getClassName(cpg)) && "append".equals(invoke.getMethodName(cpg))) {
                    Instruction prev = loc.getHandle().getPrev().getInstruction();

                    if (prev instanceof LDC) {
                        LDC ldc = (LDC)prev;
                        Object value = ldc.getValue(cpg);

                        if (value instanceof String) {
                            String v = (String)value;

                            if ("redirect:".equals(v)) {
                                BugInstance bug = new BugInstance(this, SPRING_UNVALIDATED_REDIRECT_TYPE, Priorities.NORMAL_PRIORITY);
                                bug.addClass(clazz).addMethod(clazz,m).addSourceLine(classContext,m,loc);
                                reporter.reportBug(bug);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void report() {

    }
}
