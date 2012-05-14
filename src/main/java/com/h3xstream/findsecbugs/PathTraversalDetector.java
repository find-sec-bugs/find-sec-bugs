package com.h3xstream.findsecbugs;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
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

        //API that read files

        if (seen == INVOKESPECIAL && getClassConstantOperand().equals("java/io/File")
                && getNameConstantOperand().equals("<init>")
                && (getSigConstantOperand().equals("(Ljava/lang/String;)V") || getSigConstantOperand().equals("(Ljava/lang/String;Ljava/lang/String;)V"))) {

            bugReporter.reportBug(new BugInstance(this, PATH_TRAVERSAL_IN_TYPE, NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this) //
                    .addString("java.io.File"));
        } else if (seen == INVOKESPECIAL && getClassConstantOperand().equals("java/io/RandomAccessFile")
                && getNameConstantOperand().equals("<init>")
                && (getSigConstantOperand().equals("(Ljava/lang/String;Ljava/lang/String;)V"))) {

            bugReporter.reportBug(new BugInstance(this, PATH_TRAVERSAL_IN_TYPE, NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this) //
                    .addString("java.io.RandomAccessFile"));
        } else if (seen == INVOKESPECIAL && getClassConstantOperand().equals("java/io/FileReader")
                && getNameConstantOperand().equals("<init>")
                && (getSigConstantOperand().equals("(Ljava/lang/String;)V"))) {

            bugReporter.reportBug(new BugInstance(this, PATH_TRAVERSAL_IN_TYPE, NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this) //
                    .addString("java.io.FileReader"));
        } else if (seen == INVOKESPECIAL && getClassConstantOperand().equals("java/io/FileInputStream")
                && getNameConstantOperand().equals("<init>")
                && getSigConstantOperand().equals("(Ljava/lang/String;)V")) {

            bugReporter.reportBug(new BugInstance(this, PATH_TRAVERSAL_IN_TYPE, NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this) //
                    .addString("java.io.FileInputStream"));
        }

        //API that write files

        else if (seen == INVOKESPECIAL && getClassConstantOperand().equals("java/io/FileWriter")
                && getNameConstantOperand().equals("<init>")
                && (getSigConstantOperand().equals("(Ljava/lang/String;)V") ||
                getSigConstantOperand().equals("(Ljava/lang/String;Z)V"))) {

            bugReporter.reportBug(new BugInstance(this, PATH_TRAVERSAL_OUT_TYPE, NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this) //
                    .addString("java.io.FileWriter"));
        } else if (seen == INVOKESPECIAL && getClassConstantOperand().equals("java/io/FileOutputStream")
                && getNameConstantOperand().equals("<init>")
                && (getSigConstantOperand().equals("(Ljava/lang/String;)V") ||
                getSigConstantOperand().equals("(Ljava/lang/String;Z)V"))) {

            bugReporter.reportBug(new BugInstance(this, PATH_TRAVERSAL_OUT_TYPE, NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this) //
                    .addString("java.io.FileOutputStream"));
        }
    }
}
