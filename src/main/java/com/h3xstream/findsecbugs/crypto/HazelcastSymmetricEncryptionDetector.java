package com.h3xstream.findsecbugs.crypto;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

/**
 * http://code.google.com/p/hazelcast/wiki/Encryption
 */
public class HazelcastSymmetricEncryptionDetector extends OpcodeStackDetector {
	private static final String HAZELCAST_SYMMETRIC_ENCRYPTION_TYPE = "HAZELCAST_SYMMETRIC_ENCRYPTION";

    private BugReporter bugReporter;

    public HazelcastSymmetricEncryptionDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
		//printOpCode( seen );
        if (seen == INVOKESPECIAL && getClassConstantOperand().equals("com/hazelcast/config/SymmetricEncryptionConfig") &&
		        getNameConstantOperand().equals("<init>")) {

            bugReporter.reportBug(new BugInstance(this, HAZELCAST_SYMMETRIC_ENCRYPTION_TYPE, NORMAL_PRIORITY) //
                    .addClass( this ).addMethod(this).addSourceLine(this));
        }
    }
}
