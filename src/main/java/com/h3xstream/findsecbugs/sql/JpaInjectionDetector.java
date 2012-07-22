package com.h3xstream.findsecbugs.sql;

import edu.umd.cs.findbugs.BugReporter;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.InvokeInstruction;

public class JpaInjectionDetector extends SqlInjectionDetector {

    public JpaInjectionDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    protected int[] isAcceptingSqlQuery(InvokeInstruction ins, ConstantPoolGen cpg) {

        if (ins instanceof INVOKEINTERFACE) {
            String methodName = ins.getMethodName(cpg);
            String methodSignature = ins.getSignature(cpg);
            String className = ins.getClassName(cpg);

//            System.out.println(className+"."+methodName+" - "+methodSignature);

            if (className.equals("javax.persistence.EntityManager") && methodName.equals("createQuery") &&
                    methodSignature.equals("(Ljava/lang/String;)Ljavax/persistence/Query;")) {
                return new int[]{0};
            } else if (className.equals("javax.persistence.EntityManager") && methodName.equals("createQuery") &&
                    methodSignature.equals("(Ljava/lang/String;Ljava/lang/Class;)Ljavax/persistence/TypedQuery;")) {
                return new int[]{1};
            }
        }

        return new int[0];
    }
}
