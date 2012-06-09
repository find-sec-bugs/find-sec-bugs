package com.h3xstream.findsecbugs;

import com.h3xstream.findsecbugs.common.StringTracer;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

public class PathTraversalDetector extends OpcodeStackDetector {

    private static final String PATH_TRAVERSAL_IN_TYPE = "PATH_TRAVERSAL_IN";
    private static final String PATH_TRAVERSAL_OUT_TYPE = "PATH_TRAVERSAL_OUT";

    private BugReporter bugReporter;

    public PathTraversalDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {

        if (seen == INVOKESPECIAL) { //All constructor calls

            //API that read files

            if (getClassConstantOperand().equals("java/io/File")
                    && getNameConstantOperand().equals("<init>")) {

                //Constructor with one param.
                if(getSigConstantOperand().equals("(Ljava/lang/String;)V")) {
                    if (StringTracer.isVariableString(stack.getStackItem(0))) {
                        bugReporter.reportBug(new BugInstance(this, PATH_TRAVERSAL_IN_TYPE, NORMAL_PRIORITY) //
                                                .addClass(this).addMethod(this).addSourceLine(this) //
                                                .addString("java.io.File"));
                    }
                //Constructor with two param.
                } else if(getSigConstantOperand().equals("(Ljava/lang/String;Ljava/lang/String;)V")) {

                    if (StringTracer.isVariableString(stack.getStackItem(1)) || !StringTracer.isVariableString(stack.getStackItem(0))) {
                        bugReporter.reportBug(new BugInstance(this, PATH_TRAVERSAL_IN_TYPE, NORMAL_PRIORITY) //
                                                .addClass(this).addMethod(this).addSourceLine(this) //
                                                .addString("java.io.File"));
                    }
                }


            } else if (getClassConstantOperand().equals("java/io/RandomAccessFile")
                    && getNameConstantOperand().equals("<init>")
                    && (getSigConstantOperand().equals("(Ljava/lang/String;Ljava/lang/String;)V"))) {

                if (StringTracer.isVariableString(stack.getStackItem(1)) || !StringTracer.isVariableString(stack.getStackItem(0))) {
                    bugReporter.reportBug(new BugInstance(this, PATH_TRAVERSAL_IN_TYPE, NORMAL_PRIORITY) //
                            .addClass(this).addMethod(this).addSourceLine(this) //
                            .addString("java.io.RandomAccessFile"));
                }
            } else if (getClassConstantOperand().equals("java/io/FileReader")
                    && getNameConstantOperand().equals("<init>")
                    && (getSigConstantOperand().equals("(Ljava/lang/String;)V"))) {

                if (StringTracer.isVariableString(stack.getStackItem(0))) {
                    bugReporter.reportBug(new BugInstance(this, PATH_TRAVERSAL_IN_TYPE, NORMAL_PRIORITY) //
                            .addClass(this).addMethod(this).addSourceLine(this) //
                            .addString("java.io.FileReader"));
                }
            } else if (getClassConstantOperand().equals("java/io/FileInputStream")
                    && getNameConstantOperand().equals("<init>")
                    && getSigConstantOperand().equals("(Ljava/lang/String;)V")) {

                if (StringTracer.isVariableString(stack.getStackItem(0))) {
                    bugReporter.reportBug(new BugInstance(this, PATH_TRAVERSAL_IN_TYPE, NORMAL_PRIORITY) //
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
                    bugReporter.reportBug(new BugInstance(this, PATH_TRAVERSAL_OUT_TYPE, NORMAL_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this) //
                        .addString("java.io.FileWriter"));
                }
            } else if (getClassConstantOperand().equals("java/io/FileOutputStream")
                    && getNameConstantOperand().equals("<init>")
                    && (getSigConstantOperand().equals("(Ljava/lang/String;)V") ||
                    getSigConstantOperand().equals("(Ljava/lang/String;Z)V"))) {

                OpcodeStack.Item param;
                if(getSigConstantOperand().equals("(Ljava/lang/String;Z)V")) { //Constructor : (String,Boolean)
                    param = stack.getStackItem(1); //Stack order is reverse (2 parameters)
                }
                else {
                    param = stack.getStackItem(0);
                }

                if (StringTracer.isVariableString(param)) {
                    bugReporter.reportBug(new BugInstance(this, PATH_TRAVERSAL_OUT_TYPE, NORMAL_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this) //
                        .addString("java.io.FileOutputStream"));
                }
            }
        }
    }
}
