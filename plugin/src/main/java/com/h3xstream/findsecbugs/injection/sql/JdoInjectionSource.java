package com.h3xstream.findsecbugs.injection.sql;

import com.h3xstream.findsecbugs.injection.InjectionSource;

import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.InvokeInstruction;

/**
 * API reference : http://db.apache.org/jdo/index.html
 */
public class JdoInjectionSource implements InjectionSource {

	@Override
	public boolean isCandidate( ConstantPoolGen cpg ) {
		for(int i=0;i<cpg.getSize();i++) {
			Constant cnt =cpg.getConstant( i );
			if(cnt instanceof ConstantUtf8 ) {
				String utf8String = ((ConstantUtf8) cnt).getBytes();
				//System.out.println("cnt= "+utf8String);
				if(utf8String.equals( "javax/jdo/PersistenceManager" )) {
					return true;
				}
			}
		}
		return false;
	}

    @Override
    public int[] getInjectableParameters(InvokeInstruction ins, ConstantPoolGen cpg) {
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
