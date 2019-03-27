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
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

public class Struts2EndpointDetector implements Detector {

    private static final String STRUTS2_ENDPOINT_TYPE = "STRUTS2_ENDPOINT";

    private BugReporter bugReporter;

    public Struts2EndpointDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();

        for (Method m : javaClass.getMethods()) {
            if ("execute".equals(m.getName()) && "()Ljava/lang/String;".equals(m.getSignature())) {
                bugReporter.reportBug(new BugInstance(this, STRUTS2_ENDPOINT_TYPE, Priorities.LOW_PRIORITY) //
                        .addClass(javaClass));
            }
        }
    }

    @Override
    public void report() {

    }
}
