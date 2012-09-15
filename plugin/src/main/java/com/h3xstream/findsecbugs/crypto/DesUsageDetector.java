package com.h3xstream.findsecbugs.crypto;

import com.h3xstream.findsecbugs.common.StringTracer;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;

/**
 *
 * <b>Cipher identify</b>
 *
 * <ul>
 *     <li>DES/CBC/NoPadding (56 bit)
 *     <li>DES/CBC/PKCS5Padding (56 bit)
 *     <li>DES/ECB/NoPadding (56 bit)
 *     <li>DES/ECB/PKCS5Padding (56 bit)
 *     <li>DESede/CBC/NoPadding (168 bit)
 *     <li>DESede/CBC/PKCS5Padding (168 bit)
 *     <li>DESede/ECB/NoPadding (168 bit)
 *     <li>DESede/ECB/PKCS5Padding (168 bit)
 * </ul>
 * Ref: <a href="http://docs.oracle.com/javase/7/docs/api/javax/crypto/Cipher.html">Partial list of ciphers</a>
 */
public class DesUsageDetector  extends OpcodeStackDetector {

    private static final boolean DEBUG = false;
    private static final String DES_USAGE_TYPE = "DES_USAGE";

    private BugReporter bugReporter;

    public DesUsageDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        if (seen == Constants.INVOKESTATIC && getClassConstantOperand().equals("javax/crypto/Cipher") &&
                getNameConstantOperand().equals("getInstance")) {
            OpcodeStack.Item item = stack.getStackItem( stack.getStackDepth()-1 ); //The first argument is last
            if ( StringTracer.isConstantString(item)) {
                String cipherValue = (String) item.getConstant();
                if(DEBUG) System.out.println(cipherValue);

                if(cipherValue.startsWith( "DES/" ) || cipherValue.startsWith( "DESede/" )) {
                    bugReporter.reportBug(new BugInstance(this, DES_USAGE_TYPE, Priorities.NORMAL_PRIORITY) //
                            .addClass( this ).addMethod( this ).addSourceLine(this));
                }
            }
        }
    }
}
