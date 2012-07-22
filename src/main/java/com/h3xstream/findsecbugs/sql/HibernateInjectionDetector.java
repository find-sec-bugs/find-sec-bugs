package com.h3xstream.findsecbugs.sql;

import com.h3xstream.findsecbugs.common.ByteCode;
import edu.umd.cs.findbugs.BugReporter;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.InvokeInstruction;

/**
 * Focus on hibernate API for SQL/HQL injection.
 */
public class HibernateInjectionDetector extends SqlInjectionDetector {

    public HibernateInjectionDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    protected int[] isAcceptingSqlQuery(InvokeInstruction ins, ConstantPoolGen cpg) {
        //ByteCode.printOpCode(ins, cpg);

        if (ins instanceof INVOKESTATIC || ins instanceof INVOKEINTERFACE) {
            String methodName = ins.getMethodName(cpg);
            String methodSignature = ins.getSignature(cpg);
            String className = ins.getClassName(cpg);

            //Criterion.sqlRestriction
            if (ins instanceof INVOKESTATIC && className.equals("org.hibernate.criterion.Restrictions") && methodName.equals("sqlRestriction") &&
                    methodSignature.equals("(Ljava/lang/String;)Lorg/hibernate/criterion/Criterion;")) {
                return new int[] {0};
            }
            //Session.createQuery
            else if (ins instanceof INVOKEINTERFACE && className.equals("org.hibernate.Session") && methodName.equals("createQuery") &&
                    methodSignature.equals("(Ljava/lang/String;)Lorg/hibernate/Query;")) {
                return new int[] {0};
            }
            //Session.createSQLQuery
            else if (ins instanceof INVOKEINTERFACE && className.equals("org.hibernate.Session") && methodName.equals("createSQLQuery") &&
                    methodSignature.equals("(Ljava/lang/String;)Lorg/hibernate/SQLQuery;")) {
                return new int[] {0};
            }
        }

        return new int[0];
    }
}
