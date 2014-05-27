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
public class WeakMessageDigestDetector extends OpcodeStackDetector {

    private static final String WEAK_MESSAGE_DIGEST_TYPE = "WEAK_MESSAGE_DIGEST";

    private BugReporter bugReporter;

    public WeakMessageDigestDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        //printOpCode(seen);
        if (seen == Constants.INVOKESTATIC && getClassConstantOperand().equals("java/security/MessageDigest") &&
                getNameConstantOperand().equals("getInstance") &&
                getSigConstantOperand().equals("(Ljava/lang/String;)Ljava/security/MessageDigest;")) {

            //Extract the value being push..
            OpcodeStack.Item top = stack.getStackItem(0);
            String algorithm = (String) top.getConstant(); //Null if the value passed isn't constant

            analyzeHashingFunction(algorithm);
        }

        if (seen == Constants.INVOKESTATIC && //
                getClassConstantOperand().equals("org/apache/commons/codec/digest/DigestUtils")) {
            if(getNameConstantOperand().equals("getMd2Digest") || //
                    getNameConstantOperand().equals("md2Hex")) {
                analyzeHashingFunction("md2");
            }

            if(getNameConstantOperand().equals("getMd5Digest") || //
                    getNameConstantOperand().equals("md5Hex")) {
                analyzeHashingFunction("md5");
            }

            if(getNameConstantOperand().equals("getSha1Digest") || //
                    getNameConstantOperand().equals("getShaDigest") ||
                    getNameConstantOperand().equals("sha1Hex")) {
                analyzeHashingFunction("sha1");
            }

            if(getNameConstantOperand().equals("getDigest")) {
                //Extract the value being push..
                OpcodeStack.Item top = stack.getStackItem(0);
                String algorithm = (String) top.getConstant(); //Null if the value passed isn't constant

                analyzeHashingFunction(algorithm);
            }
        }
    }

    private void analyzeHashingFunction(String algorithm) {
        if(algorithm == null) return;

        algorithm = algorithm.toUpperCase();

        if ("MD2".equals(algorithm) || "MD5".equals(algorithm)) {
            bugReporter.reportBug(new BugInstance(this, WEAK_MESSAGE_DIGEST_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this) //
                    .addString(algorithm));
        }

        if ("SHA1".equals(algorithm)) { //Lower priority for SHA-1
            bugReporter.reportBug(new BugInstance(this, WEAK_MESSAGE_DIGEST_TYPE, Priorities.LOW_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this) //
                    .addString(algorithm));
        }
    }
}
