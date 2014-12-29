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
package com.h3xstream.findsecbugs.crypto;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.classfile.JavaClass;

/**
 * Implementing a custom solution for message digest should not promote.
 * Well establish implementation are available from the JDK.
 */
public class CustomMessageDigestDetector implements Detector {

    private static final String CUSTOM_MESSAGE_DIGEST_TYPE = "CUSTOM_MESSAGE_DIGEST";

    private BugReporter bugReporter;

    public CustomMessageDigestDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }


    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();

        if ("java.security.MessageDigest".equals(javaClass.getSuperclassName())) {
            bugReporter.reportBug(new BugInstance(this, CUSTOM_MESSAGE_DIGEST_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(javaClass));
        }
    }

    @Override
    public void report() {
    }
}
