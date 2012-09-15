package com.h3xstream.findsecbugs.crypto;

import com.h3xstream.findsecbugs.common.StringTracer;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;

/**
 * Identify ECB mode for certain cipher. (AES for the moment).
 * DES ciphers are target by {@link com.h3xstream.findsecbugs.crypto.DesUsageDetector}.
 */
public class EcbModeDetector extends OpcodeStackDetector {

	private static final String ECB_MODE_TYPE = "ECB_MODE";
    private static final boolean DEBUG = false;

    private BugReporter bugReporter;

    public EcbModeDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

	@Override
	public void sawOpcode( int seen ) {
		//printOpCode( seen );

		if (seen == Constants.INVOKESTATIC && getClassConstantOperand().equals("javax/crypto/Cipher") &&
		                getNameConstantOperand().equals("getInstance")) {
			OpcodeStack.Item item = stack.getStackItem( stack.getStackDepth()-1 ); //The first argument is last
			if ( StringTracer.isConstantString(item)) {
				String cipherValue = (String) item.getConstant();
				if(DEBUG) System.out.println(cipherValue);

				if(cipherValue.contains( "AES/ECB/" )) {
					bugReporter.reportBug(new BugInstance(this, ECB_MODE_TYPE, Priorities.NORMAL_PRIORITY) //
						.addClass( this ).addMethod( this ).addSourceLine(this));
				}
			}
		}
	}
}
