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

/**
 * Identify endpoints using the web framework Wicket.
 * <p>
 * <a href="http://wicket.apache.org/">Official website</a>
 */
public class WicketEndpointDetector implements Detector {

    private static final String WICKET_ENDPOINT_TYPE = "WICKET_ENDPOINT";

    private BugReporter bugReporter;

    public WicketEndpointDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();

        String superClassName = javaClass.getSuperclassName();
        if ("org.apache.wicket.markup.html.WebPage".equals(superClassName)) {
            bugReporter.reportBug(new BugInstance(this, WICKET_ENDPOINT_TYPE, Priorities.LOW_PRIORITY) //
                    .addClass(javaClass));
            return;
        }
    }

    @Override
    public void report() {

    }
}
