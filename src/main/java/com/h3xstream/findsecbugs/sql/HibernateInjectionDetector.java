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

    protected boolean isAcceptingSqlQuery(InvokeInstruction ins, ConstantPoolGen cpg) {
        //ByteCode.printOpCode(ins, cpg);

        if (ins instanceof INVOKESTATIC || ins instanceof INVOKEINTERFACE) {
            String methodName = ins.getMethodName(cpg);
            String methodSignature = ins.getSignature(cpg);
            String className = ins.getClassName(cpg);

            //Criterion.sqlRestriction
            if (ins instanceof INVOKESTATIC && className.equals("org.hibernate.criterion.Restrictions") && methodName.equals("sqlRestriction") &&
                    methodSignature.startsWith("(Ljava/lang/String;)Lorg/hibernate/criterion/Criterion;")) {
                return true;
            }
            //Session.createQuery
            else if (ins instanceof INVOKEINTERFACE && className.equals("org.hibernate.Session") && methodName.equals("createQuery") &&
                    methodSignature.startsWith("(Ljava/lang/String;)Lorg/hibernate/Query;")) {
                return true;
            }
            //Session.createSQLQuery
            else if (ins instanceof INVOKEINTERFACE && className.equals("org.hibernate.Session") && methodName.equals("createSQLQuery") &&
                    methodSignature.startsWith("(Ljava/lang/String;)Lorg/hibernate/SQLQuery;")) {
                return true;
            }
        }

        return false;
    }
}
