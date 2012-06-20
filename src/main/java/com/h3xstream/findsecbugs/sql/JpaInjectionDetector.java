package com.h3xstream.findsecbugs.sql;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.ba.ClassContext;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InvokeInstruction;

public class JpaInjectionDetector  extends SqlInjectionDetector {

    public JpaInjectionDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
       protected boolean isAcceptingSqlQuery(InvokeInstruction ins, ConstantPoolGen cpg) {

           return false;
       }
}
