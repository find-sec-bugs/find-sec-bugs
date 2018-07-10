/**
 * Find Security Bugs
 * Copyright (c) Philippe Arteau, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package com.h3xstream.findsecbugs.crypto;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;

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
        if (seen == Const.INVOKESPECIAL && getClassConstantOperand().equals("com/hazelcast/config/SymmetricEncryptionConfig") &&
                getNameConstantOperand().equals("<init>")) {

            bugReporter.reportBug(new BugInstance(this, HAZELCAST_SYMMETRIC_ENCRYPTION_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this));
        }
    }
}
