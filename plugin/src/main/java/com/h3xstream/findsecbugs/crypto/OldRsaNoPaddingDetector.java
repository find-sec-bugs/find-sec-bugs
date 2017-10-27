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

import com.h3xstream.findsecbugs.common.StackUtils;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;

/**
 * Ref: http://cwe.mitre.org/data/definitions/780.html
 */
@Deprecated
public class OldRsaNoPaddingDetector extends OpcodeStackDetector {

    private static final String RSA_NO_PADDING_TYPE = "RSA_NO_PADDING";

    private final BugReporter bugReporter;

    public OldRsaNoPaddingDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        if (seen == Constants.INVOKESTATIC
                && getClassConstantOperand().equals("javax/crypto/Cipher")
                && getNameConstantOperand().equals("getInstance")) {
            OpcodeStack.Item item = stack.getStackItem(getSigConstantOperand().contains(";L") ? 1 : 0);
            if (StackUtils.isConstantString(item)) {
                String cipherValue = (String) item.getConstant();
                // default padding for "RSA" only is PKCS1 so it is not reported
                if (cipherValue.startsWith("RSA/") && cipherValue.endsWith("/NoPadding")) {
                    bugReporter.reportBug(new BugInstance(this, RSA_NO_PADDING_TYPE, Priorities.NORMAL_PRIORITY) //
                            .addClass(this).addMethod(this).addSourceLine(this));
                }
            }
        }
    }
}
