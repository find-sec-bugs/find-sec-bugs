package com.h3xstream.findsecbugs.injection.sql;

import com.h3xstream.findsecbugs.injection.InjectionSource;

import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.InvokeInstruction;

/**
 * API reference :
 * http://www.oracle.com/technetwork/articles/javaee/jpa-137156.html
 * http://docs.oracle.com/javaee/6/api/javax/persistence/package-summary.html
 */
public class JpaInjectionSource implements InjectionSource {

	@Override
	public boolean isCandidate( ConstantPoolGen cpg ) {
		for(int i=0;i<cpg.getSize();i++) {
			Constant cnt =cpg.getConstant( i );
			if(cnt instanceof ConstantUtf8 ) {
				String utf8String = ((ConstantUtf8) cnt).getBytes();
				//System.out.println("cnt= "+utf8String);
				if(utf8String.equals( "javax/persistence/EntityManager" )) {
					return true;
				}
			}
		}
		return false;
	}

    @Override
    public int[] getInjectableParameters(InvokeInstruction ins, ConstantPoolGen cpg) {

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
