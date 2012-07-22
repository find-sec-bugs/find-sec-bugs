package com.h3xstream.findsecbugs.sql;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.ba.ClassContext;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.InvokeInstruction;

public class JdoInjectionDetector extends SqlInjectionDetector {


    public JdoInjectionDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    protected int[] isAcceptingSqlQuery(InvokeInstruction ins, ConstantPoolGen cpg) {
        //ByteCode.printOpCode(ins, cpg);

        if (ins instanceof INVOKEINTERFACE) {
            String methodName = ins.getMethodName(cpg);
            String methodSignature = ins.getSignature(cpg);
            String className = ins.getClassName(cpg);

            if (className.equals("javax.jdo.PersistenceManager") && methodName.equals("newQuery") &&
                                methodSignature.equals("(Ljava/lang/String;)Ljavax/jdo/Query;")) {
                return new int[] {0};
            }
            else if (className.equals("javax.jdo.PersistenceManager") && methodName.equals("newQuery") &&
                                methodSignature.equals("(Ljava/lang/String;Ljava/lang/Object;)Ljavax/jdo/Query;")) {
                return new int[] {0};
            }
        }
        return new int[0];
    }


}
