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
package com.h3xstream.findsecbugs.endpoint;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.ClassContext;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class SpringMvcEndpointDetector implements Detector {

    private static final String SPRING_ENDPOINT_TYPE = "SPRING_ENDPOINT";

    private static final List<String> REQUEST_MAPPING_ANNOTATION_TYPES = Arrays.asList(
            "Lorg/springframework/web/bind/annotation/RequestMapping;", //
            "Lorg/springframework/web/bind/annotation/GetMapping;", //
            "Lorg/springframework/web/bind/annotation/PostMapping;", //
            "Lorg/springframework/web/bind/annotation/PutMapping;", //
            "Lorg/springframework/web/bind/annotation/DeleteMapping;", //
            "Lorg/springframework/web/bind/annotation/PatchMapping;");

    private BugReporter bugReporter;

    public SpringMvcEndpointDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();
        method : for (Method m : javaClass.getMethods()) {

            for (AnnotationEntry ae : m.getAnnotationEntries()) {

                if (REQUEST_MAPPING_ANNOTATION_TYPES.contains(ae.getAnnotationType())) {
                    bugReporter.reportBug(new BugInstance(this, SPRING_ENDPOINT_TYPE, Priorities.LOW_PRIORITY) //
                            .addClassAndMethod(javaClass, m));
                    continue method;
                }
            }
        }
    }

    @Override
    public void report() {

    }

}
