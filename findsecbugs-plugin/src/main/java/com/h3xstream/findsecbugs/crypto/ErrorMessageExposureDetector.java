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
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;

/**
 * Printing error messages to standard output may expose security-sensitive information,
 * and such an exposure of unencrypted information would be vulnerable as reported
 * by CWE-209 (https://cwe.mitre.org/data/deffinitions/209.html).
 *
 * This detector detects a call to printStackTrace() on exceptions that possibly contain
 * sensitive system information as a bug.
 */
public class ErrorMessageExposureDetector extends OpcodeStackDetector {

    private static final String INFORMATION_EXPOSURE_THROUGH_AN_ERROR_MESSAGE_TYPE = "INFORMATION_EXPOSURE_THROUGH_AN_ERROR_MESSAGE";
    private BugReporter bugReporter;

    public ErrorMessageExposureDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
//        printOpCode(seen);
        if (seen == Const.INVOKEVIRTUAL) {
            String fullClassName = getClassConstantOperand();
            String method = getNameConstantOperand();
            if(isVulnerableClassToPrint(fullClassName) && method.equals("printStackTrace")) {
                if (stack.getStackDepth() > 1) { // If has parameters
                    OpcodeStack.Item parameter = stack.getStackItem(0);
                    if (parameter.getSignature().equals("Ljava/io/PrintStream;") ||
                            parameter.getSignature().equals("Ljava/io/PrintWriter;")) {
                        bugReporter.reportBug(new BugInstance(this,
                                INFORMATION_EXPOSURE_THROUGH_AN_ERROR_MESSAGE_TYPE,
                                Priorities.NORMAL_PRIORITY)
                                .addClass(this).addMethod(this).addSourceLine(this));
                    }
                } else { // No parameter (only printStackTrace)
                    bugReporter.reportBug(new BugInstance(this,
                            INFORMATION_EXPOSURE_THROUGH_AN_ERROR_MESSAGE_TYPE,
                            Priorities.LOW_PRIORITY)
                            .addClass(this).addMethod(this).addSourceLine(this));
                }
            }
        }
    }

    private boolean isVulnerableClassToPrint(String classConstantOperand) {
        switch(classConstantOperand) {
            // By following the parent classes of vulnerable class to print
            case "java/lang/Throwable":
            case "java/lang/Exception":
            case "java/lang/Error":

                // By following the recommendation from the CERT Oracle Secure Coding Standard for Java
            case "java/io/FileNotFoundException":
            case "java/sql/SQLException":
            case "java/net/BindException":
            case "java/util/ConcurrentModificationException":
            case "javax/naming/InsufficientResourcesException":
            case "java/util/MissingResourceException":
            case "java/util/jar/JarException":
            case "java/security/acl/NotOwnerException":
            case "java/lang/OutOfMemoryError":
            case "java/lang/StackOverflowError":
                return true;
            default:
                return false;
        }
    }
}
