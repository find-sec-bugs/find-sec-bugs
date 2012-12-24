/**
 * Find Security Bugs
 * Copyright (c) 2013, Philippe Arteau, All rights reserved.
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
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;

/**
 * Identify the use MD2 and MD5 hashing function and recommend the
 * use of SHA functions.
 */
//TODO: Add org.apache.commons.codec.digest.DigestUtils.md5()
public class WeakMessageDigestDetector extends OpcodeStackDetector {

    private static final String WEAK_MESSAGE_DIGEST_TYPE = "WEAK_MESSAGE_DIGEST";

    private BugReporter bugReporter;

    public WeakMessageDigestDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {

        if (seen == Constants.INVOKESTATIC && getClassConstantOperand().equals("java/security/MessageDigest") &&
                getNameConstantOperand().equals("getInstance") &&
                getSigConstantOperand().equals("(Ljava/lang/String;)Ljava/security/MessageDigest;")) {

            //Extract the value being push..
            OpcodeStack.Item top = stack.getStackItem(0);
            String algorithm = (String) top.getConstant(); //Null if the value passed isn't constant

            if ("MD2".equals(algorithm) || "MD5".equals(algorithm)) {
                bugReporter.reportBug(new BugInstance(this, WEAK_MESSAGE_DIGEST_TYPE, Priorities.NORMAL_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this) //
                        .addString(algorithm));
            }

        }
    }
}
