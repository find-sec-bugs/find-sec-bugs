package com.h3xstream.findsecbugs.crypto;

import com.h3xstream.findsecbugs.common.ByteCode;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.MethodGen;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.ba.ClassContext;

public class BadHexadecimalConversionDetector implements Detector {

	private static final boolean DEBUG = false;
	private static final String BAD_HEXA_CONVERSION_TYPE = "BAD_HEXA_CONVERSION";
	private BugReporter bugReporter;

	public BadHexadecimalConversionDetector( BugReporter bugReporter ) {
		this.bugReporter = bugReporter;
	}

	@Override
	public void visitClassContext( ClassContext classContext ) {
		JavaClass javaClass = classContext.getJavaClass();

		Method[] methodList = javaClass.getMethods();

		for ( Method m : methodList ) {
			MethodGen methodGen = classContext.getMethodGen( m );

			if ( DEBUG ) {
				System.out.println( ">>> Method: " + m.getName() );
			}

			//Currently the detection is pretty weak.
			//It will catch Dummy implementation that have empty method implementation
			boolean invokeMessageDigest = false;
			boolean invokeToHexString = false;

			ConstantPoolGen cpg = classContext.getConstantPoolGen();
			for ( Instruction inst : methodGen.getInstructionList().getInstructions() ) {
				if ( DEBUG ) {
					ByteCode.printOpCode( inst, cpg );
				}

				if ( inst instanceof INVOKEVIRTUAL ) { //MessageDigest.digest is called
					INVOKEVIRTUAL invoke = (INVOKEVIRTUAL) inst;
					if("java.security.MessageDigest".equals( invoke.getClassName( cpg ) ) && "digest".equals( invoke.getMethodName( cpg ) )) {
						invokeMessageDigest = true;
					}
				}
				else if ( inst instanceof INVOKESTATIC && invokeMessageDigest ) { //The conversion must occurs after the digest was created
					INVOKESTATIC invoke = (INVOKESTATIC) inst;
					if("java.lang.Integer".equals( invoke.getClassName( cpg ) ) && "toHexString".equals( invoke.getMethodName( cpg ) )) {
						invokeToHexString = true;
					}
				}
			}

			if ( invokeMessageDigest && invokeToHexString ) {
				bugReporter.reportBug( new BugInstance( this, BAD_HEXA_CONVERSION_TYPE, NORMAL_PRIORITY ) //
						.addClassAndMethod(javaClass, m) );
			}
		}
	}

	@Override
	public void report() {

	}
}
