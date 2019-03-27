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
import java.util.regex.Pattern;
import org.apache.bcel.Const;

/**
 * This detector mark cipher usage that doesn't provide integrity.
 * <p>
 * The identification will be made base on the mode use.
 * </p>
 *
 * Bad modes:
 * <ul>
 *      <li>ECB</li>
 *      <li>CBC</li>
 *      <li>OFB</li>
 *      <li>...</li>
 * </ul>
 *
 * Safe modes:
 * <ul>
 *     <li>CCM</li>
 *     <li>CWC</li>
 *     <li>OCB</li>
 *     <li>EAX</li>
 *     <li>GCM</li>
 *  </ul>
 *
 * <p>
 * Ref: <a href="http://en.wikipedia.org/wiki/Authenticated_encryption">Wikipedia: Authenticated encryption</a>
 * Ref for the list of potential ciphers:
 * http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#impl
 * Note: Not all ECB mode are vulnerable. see RSA/ECB/*
 * </p>
 */
public class CipherWithNoIntegrityDetector extends OpcodeStackDetector {

    private static final String ECB_MODE_TYPE = "ECB_MODE";
    private static final String PADDING_ORACLE_TYPE = "PADDING_ORACLE";
    private static final String CIPHER_INTEGRITY_TYPE = "CIPHER_INTEGRITY";

    private static final Pattern AUTHENTICATED_CIPHER_MODES = Pattern.compile(".*/(CCM|CWC|OCB|EAX|GCM)/.*");
    private static final Pattern INSECURE_ECB_MODES = Pattern.compile("(AES|DES(ede)?)(/ECB/.*)?");

    private final BugReporter bugReporter;

    public CipherWithNoIntegrityDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        if ((seen != Const.INVOKESTATIC
                || !getClassConstantOperand().equals("javax/crypto/Cipher"))
                || !getNameConstantOperand().equals("getInstance")) {
            return;
        }
        OpcodeStack.Item item = stack.getStackItem(getSigConstantOperand().contains(";L") ? 1 : 0);
        String cipherValue;
        if (StackUtils.isConstantString(item)) {
            cipherValue = (String) item.getConstant();
        } else {
            return;
        }
        if (INSECURE_ECB_MODES.matcher(cipherValue).matches()) {
            reportBug(ECB_MODE_TYPE);
        }
        if (cipherValue.contains("/CBC/PKCS5Padding")) {
            reportBug(PADDING_ORACLE_TYPE);
        }

        //Some cipher will not have mode specified (ie: "RSA" .. issue GitHub #24)
        if (!AUTHENTICATED_CIPHER_MODES.matcher(cipherValue).matches()
                && !cipherValue.startsWith("RSA") && !cipherValue.equals("ECIES")  //Avoid non-block-cipher algo
                ) {
            reportBug(CIPHER_INTEGRITY_TYPE);
        }
    }

    private void reportBug(String type) {
        bugReporter.reportBug(new BugInstance(this, type, Priorities.HIGH_PRIORITY)
                .addClass(this).addMethod(this).addSourceLine(this));
    }
}
