package com.h3xstream.findsecbugs.injection.sql;

import com.h3xstream.findsecbugs.injection.InjectionSource;

import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.InvokeInstruction;

/**
 * Focus on hibernate API for SQL/HQL injection.
 */
public class HibernateInjectionSource implements InjectionSource {

	@Override
	public boolean isCandidate( ConstantPoolGen cpg ) {
		for(int i=0;i<cpg.getSize();i++) {
			Constant cnt =cpg.getConstant( i );
			if(cnt instanceof ConstantUtf8 ) {
				String utf8String = ((ConstantUtf8) cnt).getBytes();
				//System.out.println("cnt= "+utf8String);
				if(utf8String.equals( "org/hibernate/criterion/Restrictions" ) ||
						utf8String.equals( "org/hibernate/Session" )) {
					return true;
				}
			}
		}
		return false;
	}

    @Override
    public int[] getInjectableParameters(InvokeInstruction ins, ConstantPoolGen cpg) {
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
