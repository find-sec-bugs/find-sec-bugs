package com.h3xstream.findsecbugs.crypto;

import com.h3xstream.findsecbugs.common.StringTracer;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;

public class RsaNoPaddingDetector extends OpcodeStackDetector {

    private static final String RSA_NO_PADDING_TYPE = "RSA_NO_PADDING";
    private static final boolean DEBUG = false;

    private BugReporter bugReporter;

    public RsaNoPaddingDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode( int seen ) {
        if (seen == Constants.INVOKESTATIC && getClassConstantOperand().equals("javax/crypto/Cipher") &&
                getNameConstantOperand().equals("getInstance")) {
            OpcodeStack.Item item = stack.getStackItem( stack.getStackDepth()-1 ); //The first argument is last
            if ( StringTracer.isConstantString(item)) {
                String cipherValue = (String) item.getConstant();
                if(DEBUG) System.out.println(cipherValue);

                if(cipherValue.startsWith( "RSA/" ) && cipherValue.endsWith( "/NoPadding" )) {
                    bugReporter.reportBug(new BugInstance(this, RSA_NO_PADDING_TYPE, Priorities.NORMAL_PRIORITY) //
                            .addClass( this ).addMethod( this ).addSourceLine(this));
                }
            }
        }
    }
}
