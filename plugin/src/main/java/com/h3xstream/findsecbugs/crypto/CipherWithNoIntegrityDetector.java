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

import com.h3xstream.findsecbugs.common.StringTracer;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * This detector mark cipher usage that doesn't provide integrity.
 * The identification will be made base on the mode use.
 * </p>
 * <p>
 *     Bad modes:
 *     <ul>
 *         <li>ECB</li>
 *         <li>CBC</li>
 *         <li>OFB</li>
 *         <li>...</li>
 *     </ul>
 * </p>
 * <p>
 *     Safe modes:
 *     <ul>
 *         <li>CCM</li>
 *         <li>CWC</li>
 *         <li>OCB</li>
 *         <li>EAX</li>
 *         <li>GCM</li>
 *     </ul>
 * </p>
 * Ref: <a href="http://en.wikipedia.org/wiki/Authenticated_encryption">Wikipedia: Authenticated encryption</a>
 */
public class CipherWithNoIntegrityDetector extends OpcodeStackDetector {

    private static final String ECB_MODE_TYPE = "ECB_MODE";
    private static final String PADDING_ORACLE_TYPE = "PADDING_ORACLE";
    private static final String CIPHER_INTEGRITY_TYPE = "CIPHER_INTEGRITY";

    private static final boolean DEBUG = false;
    private static final List<String> SECURE_CIPHER_MODES = Arrays.asList("CCM","CWC","OCB","EAX","GCM");

    private BugReporter bugReporter;

    public CipherWithNoIntegrityDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        if (seen == Constants.INVOKESTATIC && getClassConstantOperand().equals("javax/crypto/Cipher") &&
                getNameConstantOperand().equals("getInstance")) {
            OpcodeStack.Item item = stack.getStackItem(stack.getStackDepth() - 1); //The first argument is last
            if (StringTracer.isConstantString(item)) {
                String cipherValue = (String) item.getConstant();
                if (DEBUG) System.out.println(cipherValue);

                //Ref for the list of potential ciphers : http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#impl
                //Note: Not all ECB mode are vulnerable. see RSA/ECB/*
                if (cipherValue.contains("AES/ECB/") || cipherValue.contains("DES/ECB/") || cipherValue.contains("DESede/ECB/")) {
                    bugReporter.reportBug(new BugInstance(this, ECB_MODE_TYPE, Priorities.HIGH_PRIORITY) //
                            .addClass(this).addMethod(this).addSourceLine(this));
                }

                if (cipherValue.contains("/CBC/PKCS5Padding")) {
                    bugReporter.reportBug(new BugInstance(this, PADDING_ORACLE_TYPE, Priorities.HIGH_PRIORITY) //
                            .addClass(this).addMethod(this).addSourceLine(this));
                }

                String[] cipherDefinition = cipherValue.split("/");
                if(cipherDefinition.length > 1) { //Some cipher will not have mode specified (ie: "RSA" .. issue GitHub #24)
                    String cipherMode = cipherDefinition[1];

                    if (!SECURE_CIPHER_MODES.contains(cipherMode)) { //Not a secure mode!
                        bugReporter.reportBug(new BugInstance(this, CIPHER_INTEGRITY_TYPE, Priorities.HIGH_PRIORITY) //
                                .addClass(this).addMethod(this).addSourceLine(this));
                    }
                }
            }
        }
    }
}
