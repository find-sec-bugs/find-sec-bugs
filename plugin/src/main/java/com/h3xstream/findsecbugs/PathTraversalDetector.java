/**
 * Find Security Bugs
 * Copyright (c) 2014, Philippe Arteau, All rights reserved.
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
package com.h3xstream.findsecbugs;

import com.h3xstream.findsecbugs.common.StringTracer;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;

public class PathTraversalDetector extends OpcodeStackDetector {

    private static final String PATH_TRAVERSAL_IN_TYPE = "PATH_TRAVERSAL_IN";
    private static final String PATH_TRAVERSAL_OUT_TYPE = "PATH_TRAVERSAL_OUT";

    private BugReporter bugReporter;

    public PathTraversalDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {

        if (seen == Constants.INVOKESPECIAL) { //All constructor calls

            //API that read files

            if (getClassConstantOperand().equals("java/io/File")
                    && getNameConstantOperand().equals("<init>")) {

                //Constructor with one param.
                if (getSigConstantOperand().equals("(Ljava/lang/String;)V")) {
                    if (StringTracer.isVariableString(stack.getStackItem(0))) {
                        bugReporter.reportBug(new BugInstance(this, PATH_TRAVERSAL_IN_TYPE, Priorities.NORMAL_PRIORITY) //
                                .addClass(this).addMethod(this).addSourceLine(this) //
                                .addString("java.io.File"));
                    }
                    //Constructor with two param.
                } else if (getSigConstantOperand().equals("(Ljava/lang/String;Ljava/lang/String;)V")) {

                    if (StringTracer.isVariableString(stack.getStackItem(1)) || !StringTracer.isVariableString(stack.getStackItem(0))) {
                        bugReporter.reportBug(new BugInstance(this, PATH_TRAVERSAL_IN_TYPE, Priorities.NORMAL_PRIORITY) //
                                .addClass(this).addMethod(this).addSourceLine(this) //
                                .addString("java.io.File"));
                    }
                }


            } else if (getClassConstantOperand().equals("java/io/RandomAccessFile")
                    && getNameConstantOperand().equals("<init>")
                    && (getSigConstantOperand().equals("(Ljava/lang/String;Ljava/lang/String;)V"))) {

                if (StringTracer.isVariableString(stack.getStackItem(1)) || !StringTracer.isVariableString(stack.getStackItem(0))) {
                    bugReporter.reportBug(new BugInstance(this, PATH_TRAVERSAL_IN_TYPE, Priorities.NORMAL_PRIORITY) //
                            .addClass(this).addMethod(this).addSourceLine(this) //
                            .addString("java.io.RandomAccessFile"));
                }
            } else if (getClassConstantOperand().equals("java/io/FileReader")
                    && getNameConstantOperand().equals("<init>")
                    && (getSigConstantOperand().equals("(Ljava/lang/String;)V"))) {

                if (StringTracer.isVariableString(stack.getStackItem(0))) {
                    bugReporter.reportBug(new BugInstance(this, PATH_TRAVERSAL_IN_TYPE, Priorities.NORMAL_PRIORITY) //
                            .addClass(this).addMethod(this).addSourceLine(this) //
                            .addString("java.io.FileReader"));
                }
            } else if (getClassConstantOperand().equals("java/io/FileInputStream")
                    && getNameConstantOperand().equals("<init>")
                    && getSigConstantOperand().equals("(Ljava/lang/String;)V")) {

                if (StringTracer.isVariableString(stack.getStackItem(0))) {
                    bugReporter.reportBug(new BugInstance(this, PATH_TRAVERSAL_IN_TYPE, Priorities.NORMAL_PRIORITY) //
                            .addClass(this).addMethod(this).addSourceLine(this) //
                            .addString("java.io.FileInputStream"));
                }
            }

            //API that write files

            else if (getClassConstantOperand().equals("java/io/FileWriter")
                    && getNameConstantOperand().equals("<init>")
                    && (getSigConstantOperand().equals("(Ljava/lang/String;)V") ||
                    getSigConstantOperand().equals("(Ljava/lang/String;Z)V"))) {

                if (StringTracer.isVariableString(stack.getStackItem(1))) {
                    bugReporter.reportBug(new BugInstance(this, PATH_TRAVERSAL_OUT_TYPE, Priorities.NORMAL_PRIORITY) //
                            .addClass(this).addMethod(this).addSourceLine(this) //
                            .addString("java.io.FileWriter"));
                }
            } else if (getClassConstantOperand().equals("java/io/FileOutputStream")
                    && getNameConstantOperand().equals("<init>")
                    && (getSigConstantOperand().equals("(Ljava/lang/String;)V") ||
                    getSigConstantOperand().equals("(Ljava/lang/String;Z)V"))) {

                OpcodeStack.Item param;
                if (getSigConstantOperand().equals("(Ljava/lang/String;Z)V")) { //Constructor : (String,Boolean)
                    param = stack.getStackItem(1); //Stack order is reverse (2 parameters)
                } else {
                    param = stack.getStackItem(0);
                }

                if (StringTracer.isVariableString(param)) {
                    bugReporter.reportBug(new BugInstance(this, PATH_TRAVERSAL_OUT_TYPE, Priorities.NORMAL_PRIORITY) //
                            .addClass(this).addMethod(this).addSourceLine(this) //
                            .addString("java.io.FileOutputStream"));
                }
            }
        }
    }
}
