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
 * This detector identify the usage of ESAPI cryptography components.
 * The description will list various element to verify regarding its usage : vulnerable version and weak configuration.
 */
public class EsapiEncryptorDetector extends OpcodeStackDetector {

    private static final boolean DEBUG = false;
    private static final String ESAPI_ENCRYPTOR_TYPE = "ESAPI_ENCRYPTOR";

    private BugReporter bugReporter;

    public EsapiEncryptorDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        //printOpCode(seen);
        if (seen == Const.INVOKEINTERFACE && getClassConstantOperand().equals("org/owasp/esapi/Encryptor") &&
                (getNameConstantOperand().equals("encrypt") || getNameConstantOperand().equals("decrypt"))) {

            bugReporter.reportBug(new BugInstance(this, ESAPI_ENCRYPTOR_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this));
        }
    }
}
