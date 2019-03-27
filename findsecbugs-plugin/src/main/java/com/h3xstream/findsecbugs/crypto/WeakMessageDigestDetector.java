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
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;

/**
 * Identifies the use of MD2, MD5 and SHA1 hash function and recommends the
 * use of modern functions.
 */
public class WeakMessageDigestDetector extends OpcodeStackDetector {

    private static final String WEAK_MESSAGE_DIGEST_MD5_TYPE = "WEAK_MESSAGE_DIGEST_MD5";
    private static final String WEAK_MESSAGE_DIGEST_SHA1_TYPE = "WEAK_MESSAGE_DIGEST_SHA1";

    private final BugReporter bugReporter;

    public WeakMessageDigestDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        if (seen != Const.INVOKESTATIC) {
            return;
        }
        String className = getClassConstantOperand();
        String methodName = getNameConstantOperand();
        if (className.equals("java/security/MessageDigest") && methodName.equals("getInstance")) {
            final OpcodeStack.Item item;
            String methodSig = getSigConstantOperand();
            if (methodSig.equals("(Ljava/lang/String;)Ljava/security/MessageDigest;")) {
                //Extract the value being pushed..
                item = stack.getStackItem(0);
            } else if (methodSig.equals("(Ljava/lang/String;Ljava/lang/String;)Ljava/security/MessageDigest;")) {
                item = stack.getStackItem(1);
            } else if (methodSig.equals("(Ljava/lang/String;Ljava/security/Provider;)Ljava/security/MessageDigest;")) {
                item = stack.getStackItem(1);
            } else {
                return;
            }
            String algorithm = (String) item.getConstant(); //Null if the value passed isn't constant
            checkHashFunction(algorithm);
        } else if (className.equals("org/apache/commons/codec/digest/DigestUtils")) {
            if (methodName.equals("getMd2Digest")
                    || methodName.equals("md2")
                    || methodName.equals("md2Hex")) {
                checkHashFunction("md2");
            } else if(methodName.equals("getMd5Digest")
                    || methodName.equals("md5")
                    || methodName.equals("md5Hex")) {
                checkHashFunction("md5");
            } else if(methodName.equals("getSha1Digest")
                    || methodName.equals("getShaDigest")
                    || methodName.equals("sha1")
                    || methodName.equals("sha")
                    || methodName.equals("sha1Hex")
                    || methodName.equals("shaHex")) {
                checkHashFunction("sha1");
            } else if(methodName.equals("getDigest")) {
                //Extract the value being pushed..
                OpcodeStack.Item top = stack.getStackItem(0);
                String algorithm = (String) top.getConstant(); //Null if the value passed isn't constant
                checkHashFunction(algorithm);
            }
        } else if (className.equals("java/security/Signature") && methodName.equals("getInstance")) {
            final OpcodeStack.Item item;
            String methodSig = getSigConstantOperand();
            if (methodSig.equals("(Ljava/lang/String;)Ljava/security/Signature;")) {
                item = stack.getStackItem(0);
            } else if (methodSig.equals("(Ljava/lang/String;Ljava/security/Provider;)Ljava/security/Signature;")) {
                item = stack.getStackItem(1);
            } else if (methodSig.equals("(Ljava/lang/String;Ljava/lang/String;)Ljava/security/Signature;")) {
                item = stack.getStackItem(1);
            } else {
                return;
            }
            String algorithm = (String) item.getConstant();
            if (algorithm != null) {
                int index = algorithm.indexOf("with");
                algorithm = algorithm.substring(0, index > 0 ? index : 0);
                checkHashFunction(algorithm);
            }
        }
    }

    private void checkHashFunction(String algorithm) {
        if (algorithm == null) {
            return;
        }
        algorithm = algorithm.toUpperCase();
        if ("MD2".equals(algorithm) || "MD4".equals(algorithm) || "MD5".equals(algorithm)) {
            bugReporter.reportBug(new BugInstance(this, WEAK_MESSAGE_DIGEST_MD5_TYPE, Priorities.HIGH_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this) //
                    .addString(algorithm));
        } else if ("SHA1".equals(algorithm) || "SHA-1".equals(algorithm) || "SHA".equals(algorithm)) { //Lower priority for SHA-1
            bugReporter.reportBug(new BugInstance(this, WEAK_MESSAGE_DIGEST_SHA1_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this) //
                    .addString(algorithm));
        }
    }
}
