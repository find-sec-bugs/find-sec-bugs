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
 * <b>Cipher identify</b>
 *
 * <ul>
 * <li>DES/CBC/NoPadding (56 bit)</li>
 * <li>DES/CBC/PKCS5Padding (56 bit)</li>
 * <li>DES/ECB/NoPadding (56 bit)</li>
 * <li>DES/ECB/PKCS5Padding (56 bit)</li>
 * <li>DESede/CBC/NoPadding (168 bit)</li>
 * <li>DESede/CBC/PKCS5Padding (168 bit)</li>
 * <li>DESede/ECB/NoPadding (168 bit)</li>
 * <li>DESede/ECB/PKCS5Padding (168 bit)</li>
 * </ul>
 *
 * Ref: <a href="http://docs.oracle.com/javase/7/docs/api/javax/crypto/Cipher.html">Partial list of ciphers</a>
 */
public class DesUsageDetector extends OpcodeStackDetector {

    private static final boolean DEBUG = false;
    private static final String DES_USAGE_TYPE = "DES_USAGE";

    private BugReporter bugReporter;

    public DesUsageDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        //printOpCode(seen);

        //Cover Cipher.getInstance(...)
        if (seen == Constants.INVOKESTATIC) {
            if(getClassConstantOperand().equals("javax/crypto/Cipher") && getNameConstantOperand().equals("getInstance")) {
                OpcodeStack.Item item;
                if (getSigConstantOperand().equals("(Ljava/lang/String;)Ljavax/crypto/Cipher;")) {
                    item = stack.getStackItem(0); //The first argument is last
                } else if (getSigConstantOperand().equals("(Ljava/lang/String;Ljava/lang/String;)Ljavax/crypto/Cipher;")) {
                    item = stack.getStackItem(1);
                } else if (getSigConstantOperand().equals("(Ljava/lang/String;Ljava/security/Provider;)Ljavax/crypto/Cipher;")) {
                    item = stack.getStackItem(1);
                } else {
                    return;
                }


                if (StackUtils.isConstantString(item)) {
                    String cipherValue = ((String) item.getConstant()).toLowerCase();

                    if (cipherValue.equals("des") || cipherValue.startsWith("des/") || cipherValue.startsWith("desede/")) {
                        bugReporter.reportBug(new BugInstance(this, DES_USAGE_TYPE, Priorities.NORMAL_PRIORITY) //
                                .addClass(this).addMethod(this).addSourceLine(this));
                    }
                }
            }
            if(getClassConstantOperand().equals("javax/crypto/KeyGenerator") && getNameConstantOperand().equals("getInstance")) {
                OpcodeStack.Item item;
                if (getSigConstantOperand().equals("(Ljava/lang/String;)Ljavax/crypto/KeyGenerator;")) {
                    item = stack.getStackItem(0); //The first argument is last
                } else if (getSigConstantOperand().equals("(Ljava/lang/String;Ljava/lang/String;)Ljavax/crypto/KeyGenerator;")) {
                    item = stack.getStackItem(1);
                } else if (getSigConstantOperand().equals("(Ljava/lang/String;Ljava/security/Provider;)Ljavax/crypto/KeyGenerator;")) {
                    item = stack.getStackItem(1);
                } else {
                    return;
                }

                if (StackUtils.isConstantString(item)) {
                    String cipherValue = ((String) item.getConstant()).toLowerCase();

                    if (cipherValue.equals("des") || cipherValue.equals("desede")) {
                        bugReporter.reportBug(new BugInstance(this, DES_USAGE_TYPE, Priorities.NORMAL_PRIORITY) //
                                .addClass(this).addMethod(this).addSourceLine(this));
                    }
                }
            }
        }
    }
}
